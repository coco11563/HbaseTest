package chapter12;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.IOException;
import java.io.InputStreamReader;

import jcifs.smb.SmbFile;
import jcifs.smb.SmbFileInputStream;
import org.apache.log4j.Logger;

import json.JSONArray;
import json.JSONException;
import json.JSONObject;

public class Read {
	private static Logger logger = Logger.getLogger(Test.class);  

		/**
		 * 读取我们的json数据
		 * @param path
		 * @return
		 * @throws IOException
		 * @throws JSONException
		 */
		public static JSONArray read_jsonFile ( String path ) throws IOException,JSONException
		{
			JSONArray json_array = new JSONArray ( ) ;
			File file = new File ( path ) ;
			BufferedReader reader = new BufferedReader ( new FileReader ( file ) ) ;
			String tempString = null ;
			while ( ( tempString = reader.readLine ( ) ) != null )
			{
				json_array.put ( new JSONObject ( tempString ) ) ;
				}
			reader.close ( ) ;
			return json_array ;
			}

		public static JSONArray read_jsonFile ( File file ) throws IOException,JSONException
		{
			JSONArray json_array = new JSONArray ( ) ;
			BufferedReader reader = new BufferedReader ( new FileReader ( file ) ) ;
			String tempString = null ;
			while ( ( tempString = reader.readLine ( ) ) != null )
			{
				json_array.put ( new JSONObject ( tempString ) ) ;
				}
			reader.close ( ) ;
			return json_array ;
			}
		/**
		 * 读取文件
		 * @param path
		 * @return
		 * @throws JSONException 
		 */
		public static String readJson(String path) throws JSONException{
			 //从给定位置获取文件
	        File file = new File(path);
	        BufferedReader reader = null;
	        //返回值,使用StringBuffer
	        StringBuffer data = new StringBuffer();
	        //
	        try {
	            reader = new BufferedReader(new FileReader(file));
	            //每次读取文件的缓存
	            String temp = null;
	            while((temp = reader.readLine()) != null){
	                data.append(temp);
	            }
	        } catch (FileNotFoundException e) {
	            e.printStackTrace();
	        } catch (IOException e) {
	            e.printStackTrace();
	        }finally {
	            //关闭文件流
	            if (reader != null){
	                try {
	                    reader.close();
	                } catch (IOException e) {
	                    e.printStackTrace();
	                }
	            }
	        }
	        return data.toString();
	    }
		/**
		 * @author coco1
		 * @param file
		 * @return
		 * @throws JSONException
		 * 输入为文件的读取方式
		 */
		public static String readJson(File file) throws JSONException{
			 //从给定位置获取文件
	        
	        BufferedReader reader = null;
	        //返回值,使用StringBuffer
	        StringBuffer data = new StringBuffer();
	        //
	        try {
	            reader = new BufferedReader(new FileReader(file));
	            //每次读取文件的缓存
	            String temp = null;
	            while((temp = reader.readLine()) != null){
	                data.append(temp);
	            }
	        } catch (FileNotFoundException e) {
	            e.printStackTrace();
	        } catch (IOException e) {
	            e.printStackTrace();
	        }finally {
	            //关闭文件流
	            if (reader != null){
	                try {
	                    reader.close();
	                } catch (IOException e) {
	                    e.printStackTrace();
	                }
	            }
	        }
	        return data.toString();
	    }
		/**
		 * @author coco1
		 * @param file
		 * @param encode
		 * @return
		 * @throws JSONException
		 * 可以自定义读取方式的readJSON
		 */
		public static String readJson(File file, String encode) throws JSONException{
			 //从给定位置获取文件
			FileInputStream reader = null;
			InputStreamReader isr = null ;
			StringBuffer data = new StringBuffer();
			try{
			 reader =  new FileInputStream(file);
	        //返回值,使用StringBuffer
	         isr = new InputStreamReader(reader,encode);  
	         BufferedReader br = new BufferedReader(isr);  
	         
	         String line = null;   
	         
	         while ((line = br.readLine()) != null) {   
	        	 data.append(line);
	         }
	        
			}catch (FileNotFoundException e) {
	            e.printStackTrace();
	        } catch (IOException e) {
	            e.printStackTrace();
	        }
			finally
	         {
	            //关闭文件流
	            if (reader != null){
	                try {
	                    reader.close();
	                } catch (IOException e) {
	                    e.printStackTrace();
	                }
	            }
	        }
			removeFile(file);
			logger.info(data.length());
			
	        return data.toString();
	    }
		/**
	 * 以自定义的字符类型读取jsonarray
	 * @param file
	 * @param encode
	 * @return
	 * @throws IOException
	 * @throws JSONException
	 */
	public static JSONArray read_jsonFile ( File file,String encode ) throws IOException,JSONException
	{
		JSONArray json_array = new JSONArray ( ) ;
		FileInputStream reader = null;
		InputStreamReader isr = null ;
		StringBuffer data = new StringBuffer();
		try{
			reader =  new FileInputStream(file);
			//返回值,使用StringBuffer
			isr = new InputStreamReader(reader,encode);
			BufferedReader br = new BufferedReader(isr);

			String line = null;

			while ((line = br.readLine()) != null) {
				//GC OVER FLOW
				json_array.put ( new JSONObject ( line ) ) ;
			}

		}catch (FileNotFoundException e) {
			e.printStackTrace();
		} catch (IOException e) {
			e.printStackTrace();
		}
		finally
		{
			//关闭文件流
			if (reader != null){
				try {
					reader.close();
				} catch (IOException e) {
					e.printStackTrace();
				}
			}
		}
		removeFile(file);
		logger.info(json_array.length());
		return json_array ;
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
	 * 以自定义的字符类型读取jsonarray
	 * @param file
	 * @param encode
	 * @return
	 * @throws IOException
	 * @throws JSONException
	 */
	public static JSONArray read_jsonFile (SmbFile file, String encode ) throws IOException,JSONException
	{
		JSONArray json_array = new JSONArray ( ) ;
		SmbFileInputStream reader = null;
		InputStreamReader isr = null ;
		StringBuffer data = new StringBuffer();
		try{
			reader =  new SmbFileInputStream(file);
			//返回值,使用StringBuffer
			isr = new InputStreamReader(reader,encode);
			BufferedReader br = new BufferedReader(isr);

			String line = null;

			while ((line = br.readLine()) != null) {
				//GC OVER FLOW
				json_array.put ( new JSONObject ( line ) ) ;
			}

		}catch (FileNotFoundException e) {
			e.printStackTrace();
		} catch (IOException e) {
			e.printStackTrace();
		}
		finally
		{
			//关闭文件流
			if (reader != null){
				try {
					reader.close();
				} catch (IOException e) {
					e.printStackTrace();
				}
			}
		}
		logger.info(json_array.length());
		return json_array ;
	}
}
