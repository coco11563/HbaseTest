package HbaseImporter;
import HbaseUtil.HbaseOperation;
import jcifs.smb.SmbFile;
import net.sf.json.JSONArray;
import net.sf.json.JSONException;
import net.sf.json.JSONObject;
import org.apache.hadoop.conf.Configuration;
import org.apache.hadoop.hbase.HBaseConfiguration;
import org.apache.hadoop.hbase.client.*;
import org.apache.hadoop.hbase.util.Bytes;
import org.apache.log4j.Logger;

import java.io.BufferedWriter;
import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.*;

import static HbaseImporter.ConfigurePart.Inial.getJsonData;
import static HbaseImporter.ZipPart.GeiFileStatus.showAllFiles;
import static HbaseImporter.ZipPart.ZipUtils.createSmbZip;
import static HbaseUtil.HbaseOperation.columnFamily;

public class HbaseImporter {
    //static������ʼ��
    private final static String tmpfilepath = "./tmp";
    private final static String cityNumPath = "./conf/cityNum.json";
    private final static String timesetpath = "./conf/timeSetting.json";
    private final static String completeddate = "./conf/CompletedDate.json";
    private final static String completednum = "./conf/completednum.json";

    // ��ȡHBaseConfiguration
    static Configuration cfg = HBaseConfiguration.create();



    //log4j initial

    private static Logger logger = Logger.getLogger(HbaseImporter.class);

    /**
     *
     * @param d ����
     * @param m ���ӵ�����
     * @return �����������������
     */
    public static String dateplus(Date d , int m)
    {
        Calendar cal = Calendar.getInstance();
        cal.setTime(d);
        cal.add(Calendar.DATE, m);
        SimpleDateFormat df = new SimpleDateFormat("yyyy-mm-dd");
        String returntype = df.format((cal.getTime()));
        return returntype;
    }
    @SuppressWarnings("deprecation")
    public static void main(String[] agrs) throws JSONException, ParseException, IOException
    {

        // ��ȡ���е�ID JSON�ļ�
        JSONObject cityNumObject = JSONObject.fromObject(Read.readJson(cityNumPath));

        JSONObject timesetting = JSONObject.fromObject(Read.readJson(timesetpath));
        //������ʼ��
        SimpleDateFormat df = new SimpleDateFormat("yyyy-mm-dd");
        Date start = df.parse(timesetting.getString("start-time"));
        Date end =  df.parse(timesetting.getString("end-time"));
        int days = (int)((end.getTime() - start.getTime())/86400000) + 1;
        logger.info("��ʼ����" + days+ "�������д��. . . ");
        // ��ʼ����hbase��д
        JSONArray inputjson = new JSONArray();
        for(int iter = 0 ; iter < days ; iter ++)//����
        {
            int stornum = 0; //�洢д������
            long start_oneday_time = new Date().getTime();
            String smbstring = "smb://biggrab:123456@192.168.1.111/biggrab/export/"+dateplus(start,iter)+"/";
            String smbzipstring = "smb://biggrab:123456@192.168.1.111/biggrab/export/"+dateplus(start,iter)+".zip";
            SmbFile fs = new SmbFile(smbstring);
            List<String> filestatus = showAllFiles(fs);

//			for(int test = 0 ; test < filestatus.size() ; test ++)
//			{		if(filestatus.get(test).split("/").length == 8) {
//				System.out.println(filestatus.get(test));
//				System.out.println((filestatus.get(test).split("/")[7]));
//			}
//				else{
//				System.out.println(filestatus.get(test));
//				System.out.println((filestatus.get(test).split("/")[6]));
//			}
//			}
//			Threads.sleep(10000);
            for (int i = 0; i < filestatus.size(); i++) {//�����б���
                String tablename = "city_";
                //File f = GetFileStatus.Save_smb(filestatus.get(i), tmpfilepath);
                //String tablename = "city_"+(cityNumObject.getString(((f.getName()).split("\\."))[0]));
                //logger.info("������:"+((f.getName()).split("\\."))[0]);
                //logger.info("����:"+cityNumObject.getString(((f.getName()).split("\\."))[0]));
                if(filestatus.get(i).split("/").length == 8) {
                    tablename = tablename + (cityNumObject.getString((filestatus.get(i).split("/")[7]).split("\\.")[0]));
                    logger.info("������:" + ((filestatus.get(i).split("/"))[7]).split("\\.")[0]);
                    logger.info("����:" + tablename);
                }
                else
                {
                    tablename = tablename + (cityNumObject.getString((filestatus.get(i).split("/")[6]).split("\\.")[0]));
                    logger.info("������:" + ((filestatus.get(i).split("/"))[6]).split("\\.")[0]);
                    logger.info("����:" + tablename);
                }
                try {
                    SmbFile remotefs = new SmbFile(filestatus.get(i));
                    inputjson = Read.read_jsonFile(remotefs,"utf-8");
                    stornum += inputjson.size() ;
                    HbaseOperation.create(tablename,columnFamily);
                    HTable cityTable = new HTable(cfg,tablename);
                    ArrayList<Put> putDateList = new ArrayList<Put>();
                    for(int rownum = 0 ; rownum < inputjson.size() ; rownum ++)//����������
                    {
                        HashMap<String,String> array = getJsonData(inputjson.getJSONObject(rownum));
                        String UserID = getUserID(array.get("user"));
                        String WeiboID = array.get("idstr");
                        String rowname = dateplus(start,iter) + "_" + UserID + "_" + WeiboID;
                        Put p1 = new Put(Bytes.toBytes(rowname));
                        //������д��
                        p1 = HbaseOperation.put(tablename, rowname, columnFamily, array);
                        putDateList.add(p1);
                        if (putDateList.size() > 1000){
                            cityTable.put(putDateList);
                            cityTable.flushCommits();
                            putDateList.clear();
                            logger.info("����һ��д��");
                        }
                    }
                    cityTable.put(putDateList);
                    cityTable.flushCommits();
                    putDateList.clear();
                    logger.info("��β������һ��д��");
                    inputjson = null ;
                    cityTable.close();

                }

                catch (Exception e) {
                    e.printStackTrace();

                }

            }


            //���һ���д��
            String times = Integer.toString(iter);
            long end_oneday_time = new Date().getTime();
            long use_time = (end_oneday_time - start_oneday_time)/(1000*60);//���Ӵ洢����ʱ��
            try {
                numInsert(dateplus(start,iter),stornum) ;
                dateInsert((times+"_"+use_time), dateplus(start,iter));
            } catch (Exception e) {
                // TODO Auto-generated catch block
                e.printStackTrace();
            }
            stornum = 0;
            //��ʼѹ����ҵ
            logger.info("start to zip the file");
            createSmbZip(smbstring,smbzipstring);
            logger.info("zip over");
            fs.delete();
        }
    }

    @SuppressWarnings("unused")
    private static void dateInsert(String iter,String date) throws JSONException, IOException {
        // TODO Auto-generated method stub
        JSONObject cd = new JSONObject();
        cd.element("completedate_" + iter,date );
        File f = new File(completeddate);
        if(!f.exists()){
            f.createNewFile();
        }
        FileWriter fileWritter = new FileWriter(f.getName(),true);
        BufferedWriter bufferWritter = new BufferedWriter(fileWritter);
        bufferWritter.write(cd.toString());
        bufferWritter.close();

        logger.info("Done One day");
    }
    private static void numInsert(String date,int stornum) throws JSONException, IOException {
        // TODO Auto-generated method stub
        JSONObject cd = new JSONObject();
        cd.element(date,stornum );
        File f = new File(completednum);
        if(!f.exists()){
            f.createNewFile();
        }
        FileWriter fileWritter = new FileWriter(f.getName(),true);
        BufferedWriter bufferWritter = new BufferedWriter(fileWritter);
        bufferWritter.write(cd.toString());
        bufferWritter.close();

        logger.info("log one day num");
    }
    private static String getUserID(String JsonString) throws JSONException {
        // TODO Auto-generated method stub
        JSONObject j = JSONObject.fromObject(JsonString);
        String gender = j.getString("gender");
        String id = j.getString("id");
        String renturnstring = gender + "_" +  id;
        return renturnstring;
    }
}
