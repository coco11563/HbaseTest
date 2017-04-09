package HbaseImporter;
import GeoIndex.datastruct.KeySizeException;
import HbaseUtil.HbaseOperation;
import jcifs.smb.SmbFile;
import net.sf.json.JSONArray;
import net.sf.json.JSONException;
import net.sf.json.JSONObject;
import org.apache.hadoop.conf.Configuration;
import org.apache.hadoop.hbase.HBaseConfiguration;
import org.apache.hadoop.hbase.client.HTable;
import org.apache.hadoop.hbase.client.Put;
import org.apache.log4j.Logger;

import java.io.BufferedWriter;
import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.List;


import static GeoIndex.main.CityGetter.inial;
import static HbaseImporter.ZipPart.GetFileStatus.showAllFiles;

import static HbaseUtil.HbaseOperation.columnFamily;

public class HbaseImporter {
    private final static String cityNumPath = "./conf/cityNum.json";
    private final static String timesetpath = "./conf/timeSetting.json";
    private final static String completeddate = "./conf/CompletedDate.json";
    private final static String completednum = "./conf/completednum.json";

    // ��ȡHBaseConfiguration
    static Configuration cfg = HBaseConfiguration.create();


    private static String TableName = "SinaWeiboDataStorage";
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
        SimpleDateFormat df = new SimpleDateFormat("yyyy-MM-dd");
        String returntype = df.format((cal.getTime()));
        return returntype;
    }
    @SuppressWarnings("deprecation")
    public static void main(String[] agrs) throws JSONException, ParseException, IOException, KeySizeException {
        inial();
        logger.debug("��ɳ�ʼ��KNN����");
        // ��ȡ���е�ID JSON�ļ�
        JSONObject cityNumObject = JSONObject.fromObject(Read.readJson(cityNumPath));
        JSONObject timesetting = JSONObject.fromObject(Read.readJson(timesetpath));
        //������ʼ��
        SimpleDateFormat df = new SimpleDateFormat("yyyy-MM-dd");
        Date start = df.parse(timesetting.getString("start-time"));
        Date end =  df.parse(timesetting.getString("end-time"));
        int days = (int)((end.getTime() - start.getTime())/86400000) + 1;
        logger.debug("��ʼ����" + days+ "�������д��. . . ");
        // ��ʼ����hbase��д
        JSONArray inputjson = new JSONArray();
        for(int iter = 0 ; iter < days ; iter ++)//����
        {
            int stornum = 0; //�洢д������
            long start_oneday_time = new Date().getTime();
            String smbstring = "smb://biggrab:123456@192.168.1.111/biggrab/export/"+dateplus(start,iter)+"/";
            String smbzipstring = "smb://biggrab:123456@192.168.1.111/biggrab/export/"+dateplus(start,iter)+".zip";
            SmbFile fs = new SmbFile(smbstring);
            System.out.println(smbstring);
            if (!fs.exists()) {
                continue;
            }
            List<String> filestatus = showAllFiles(fs);
            for (String filestatu : filestatus) {//�����б���

                if (filestatu.split("/").length == 8) {
                    logger.debug("������:" + ((filestatu.split("/"))[7]).split("\\.")[0]);
                    logger.debug("����:" + TableName);
                } else {
                    logger.debug("������:" + ((filestatu.split("/"))[6]).split("\\.")[0]);
                    logger.debug("����:" + TableName);
                }
                try {
                    SmbFile remotefs = new SmbFile(filestatu);
                    inputjson = Read.read_jsonFile(remotefs, "utf-8");
                    stornum += inputjson.size();
                    HbaseOperation.create(TableName, columnFamily);
                    HTable cityTable = new HTable(cfg, TableName);
                    ArrayList<Put> putDateList = new ArrayList<Put>();
                    for (int rownum = 0; rownum < inputjson.size(); rownum++)//����������
                    {
                        HbaseCeller hbaseCeller = new HbaseCeller(inputjson.getJSONObject(rownum));
                        Put p1;
                        //������д��
                        p1 = HbaseOperation.put(hbaseCeller, columnFamily);

                        putDateList.add(p1);
                        if (putDateList.size() > 1000) {
                            cityTable.put(putDateList);
                            cityTable.flushCommits();
                            putDateList.clear();
                            logger.debug("����һ��д��");
                        }
                    }
                    cityTable.put(putDateList);
                    cityTable.flushCommits();
                    putDateList.clear();
                    logger.debug("��β������һ��д��");
                    cityTable.close();
                } catch (Exception e) {
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
//            //��ʼѹ����ҵ
//            logger.info("start to zip the file");
//            createSmbZip(smbstring,smbzipstring);
//            logger.info("zip over");
//            fs.delete();
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
