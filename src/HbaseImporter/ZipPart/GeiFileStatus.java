package HbaseImporter.ZipPart;
import java.io.BufferedInputStream;
import java.io.BufferedOutputStream;
import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.List;

import HbaseImporter.Read;
import HbaseImporter.TestAndOld.HbaseImporter;
import net.sf.json.JSONArray;
import net.sf.json.JSONObject;
import org.apache.log4j.Logger;

import jcifs.smb.*;

import static HbaseImporter.ConfigurePart.Inial.getJsonData;

public class GeiFileStatus {
    private static Logger logger = Logger.getLogger(HbaseImporter.class);
    private final static String tmpfilepath = "./tmp";
    private final static String cityNumPath="./conf/cityNum.json";
    /*public static Date dateplus(Date d)
    {
        Calendar cal = Calendar.getInstance();
        cal.setTime(d);
        cal.add(Calendar.DATE, 2);
         SimpleDateFormat df = new SimpleDateFormat("yyyy-mm-dd");
        return cal.getTime();
    }*/
    public static void main(String[] args) throws Exception
    {
	/*read json test*/

/*	JSONObject cityNumObject = new JSONObject(Read.readJson(cityNumPath));
	System.out.println(cityNumObject.get("武汉市"));*/

	/*smb test*/

        SmbFile fs = new SmbFile("smb://biggrab:123456@192.168.1.111/biggrab/export/2016-03-06/");
        List<String> list = showAllFiles(fs);
        File fs1 = Save_smb(list.get(1),tmpfilepath);
        logger.info((fs1.getName()).split("\\.")[0]);
        JSONObject timesetting = JSONObject.fromObject(Read.readJson("D:\\库\\文档\\eclipse workspace\\HbaseTest\\conf\\timeSetting.json"));
        //获取本次起止时间
        SimpleDateFormat df = new SimpleDateFormat("yyyy-mm-dd");
        Date start = df.parse("2016-03-07");
        Date end =  df.parse("2016-03-09");
//	end = dateplus(end);
        int days = (int)((end.getTime() - start.getTime())/86400000) + 1;
        logger.info(days);
        JSONArray cityNumObject = Read.read_jsonFile(Save_smb(list.get(2),tmpfilepath),"utf-8");

        HashMap<String,String> array = getJsonData(cityNumObject.getJSONObject(1));


        logger.info("thumbnail_pic"+":"+array.get("thumbnail_pic"));


    }
    /**
     * 删除下载的文件
     * @param file
     * @return
     */

    public static boolean removeFile(File file) {
        return file.delete();
    }

    /**
     * 下载文件到临时文件夹
     * @param smbfile
     * @param tmpfilepath
     * @return 下载后的文件地址
     */
    public static File Save_smb(String smbfile,String tmpfilepath)
    {
        File localfile=null;
        InputStream bis=null;
        OutputStream bos=null;
        try{
            SmbFile rmifile = new SmbFile(smbfile);
            String filename=rmifile.getName();
            bis=new BufferedInputStream(new SmbFileInputStream(rmifile));
            localfile=new File(tmpfilepath+File.separator+filename);
            bos=new BufferedOutputStream(new FileOutputStream(localfile));
            double length=rmifile.getContentLength();
            logger.info("缓存文件大小="+length/(1024*1024)+"//MB");
            byte[] buffer=new byte[(int)length];
            Date date=new Date();
            bis.read(buffer);
            bos.write(buffer);
            Date end=new Date();
            int time= (int) ((end.getTime()-date.getTime())/1000);
            if(time>0)
                logger.info("用时:"+time+"秒 "+"速度:"+length/time/1024+"kb/秒");
        } catch (Exception e){
            logger.error(e.getMessage());

        }finally{
            try {
                bos.close();
                bis.close();
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
        return localfile;
    }
    /**
     *
     * @param dir local path File
     * @return local path list
     * @throws Exception
     * @author coco1
     */
    public final static List<String> showAllFiles(File dir) throws Exception{
        List<String> returnDir = new ArrayList<String>();
        File[] fs = dir.listFiles();
        for(int i=0; i<fs.length; i++){

            if(fs[i].isDirectory()){
                try{
                    returnDir.addAll(showAllFiles(fs[i]));
                }catch(Exception e){
                    logger.error(e.getMessage());
                }
            }
            else
            {
                returnDir.add(fs[i].getAbsolutePath());
            }
        }
        return returnDir;
    }
    /**
     *
     * @param fs2 SmbFile fs2
     * @return List of remote file system
     * @throws SmbException
     * @author coco1
     */
    public final static List<String> showAllFiles(SmbFile fs2) throws SmbException
    {
        List<String> returnDir = new ArrayList<String>();
        SmbFile[] fs = fs2.listFiles();
        for(int i=0; i<fs.length; i++){

            if(fs[i].isDirectory()){
                try{

                    returnDir.addAll(showAllFiles(fs[i]));
                }catch(Exception e){
                    logger.error(e.getMessage());
                }
            }
            else
            {

                returnDir.add(fs[i].getPath());
            }
        }
        return returnDir;}
}
