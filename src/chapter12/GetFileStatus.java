package chapter12;

import java.io.BufferedInputStream;
import java.io.BufferedOutputStream;
import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.util.ArrayList;
import java.util.Date;
import java.util.Iterator;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;
import java.util.TreeMap;

import json.JSONArray;
import json.JSONObject;
import jcifs.smb.*;
  
public class GetFileStatus{
private final static String tmpfilepath = "D:\\库\\文档\\eclipse workspace\\HbaseTest\\tmp";
	private final static String cityNumPath="D:\\库\\文档\\eclipse workspace\\HbaseTest\\conf\\cityNum.json";
public static void main(String[] args) throws Exception
{
	/*read json test*/
	
/*	JSONObject cityNumObject = new JSONObject(Read.readJson(cityNumPath));
	System.out.println(cityNumObject.get("武汉市"));*/
	
	/*smb test*/
	
	SmbFile fs = new SmbFile("smb://biggrab:123456@192.168.1.111/biggrab/export/2016-03-06/");
	List<String> list = showAllFiles(fs);
	
	JSONArray cityNumObject = Read.read_jsonFile(Save_smb(list.get(2),tmpfilepath),"utf-8");
	
	LinkedHashMap<String,String> array = SinaJsonRead.getJsonData(cityNumObject.getJSONObject(1));

	
		System.out.println("thumbnail_pic"+":"+array.get("thumbnail_pic"));
	
	
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
 * @return
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
           System.out.println("localfile=="+localfile);  
           bos=new BufferedOutputStream(new FileOutputStream(localfile));     
           int length=rmifile.getContentLength();    
           System.out.println("length=="+length);  
           byte[] buffer=new byte[length];     
           Date date=new Date();     
           bis.read(buffer);    
           bos.write(buffer);   
           Date end=new Date();     
           int time= (int) ((end.getTime()-date.getTime())/1000);     
           if(time>0)     
               System.out.println("用时:"+time+"秒 "+"速度:"+length/time/1024+"kb/秒");                 
       } catch (Exception e){      
           System.out.println(e.getMessage());     
                
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
 * @param local path File
 * @return local path list
 * @throws Exception
 */
final static List<String> showAllFiles(File dir) throws Exception{
	List<String> returnDir = new ArrayList<String>();
	  File[] fs = dir.listFiles();
	  for(int i=0; i<fs.length; i++){
		 
	   if(fs[i].isDirectory()){
	    try{
	    	returnDir.addAll(showAllFiles(fs[i]));
	    }catch(Exception e){}
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
 * @param SmbFile fs2
 * @return List of remote file system
 * @throws SmbException
 */
final static List<String> showAllFiles(SmbFile fs2) throws SmbException
{
	List<String> returnDir = new ArrayList<String>();
	  SmbFile[] fs = fs2.listFiles();
	  for(int i=0; i<fs.length; i++){
		 
	   if(fs[i].isDirectory()){
	    try{
	    
	    	returnDir.addAll(showAllFiles(fs[i]));
	    }catch(Exception e){}
	   }
	   else
	   {
	    
		   returnDir.add(fs[i].getPath());
	   }
	  }
	  return returnDir;}
}
