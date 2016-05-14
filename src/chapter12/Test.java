package chapter12;

import java.io.BufferedWriter;
import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Set;

import org.apache.hadoop.conf.Configuration;
import org.apache.hadoop.hbase.HBaseConfiguration;
import org.apache.hadoop.hbase.HColumnDescriptor;
import org.apache.hadoop.hbase.HTableDescriptor;
import org.apache.hadoop.hbase.client.Get;
import org.apache.hadoop.hbase.client.HBaseAdmin;
import org.apache.hadoop.hbase.client.HTable;
import org.apache.hadoop.hbase.client.Put;
import org.apache.hadoop.hbase.client.Result;
import org.apache.hadoop.hbase.client.ResultScanner;
import org.apache.hadoop.hbase.client.Scan;
import org.apache.hadoop.hbase.util.Bytes;

import json.JSONArray;
import json.JSONException;
import json.JSONObject;
import jcifs.smb.*;

public class Test {
	//static变量初始化
	private final static String tmpfilepath = "D:\\库\\文档\\eclipse workspace\\HbaseTest\\tmp";
	private final static String cityNumPath = "D:\\库\\文档\\eclipse workspace\\HbaseTest\\conf\\cityNum.json";
	private final static String timesetpath = "D:\\库\\文档\\eclipse workspace\\HbaseTest\\conf\\timeSetting.json";
	private final static String completeddate = "D:\\库\\文档\\eclipse workspace\\HbaseTest\\conf\\CompletedDate.json";
	// 获取HBaseConfiguration
	static Configuration cfg = HBaseConfiguration.create();
	private static final String columnFamily = "sinadata";
	// HBaseAdmin HTableDescriptor
	@SuppressWarnings("deprecation")
	public static void create(String tableName)throws IOException {
		System.out.println("正在创建"+tableName);
		HBaseAdmin admin = new HBaseAdmin(cfg);
		if (!admin.tableExists(tableName)) {
			HTableDescriptor tableDesc = new HTableDescriptor(tableName);
			tableDesc.addFamily(new HColumnDescriptor(columnFamily));
			admin.createTable(tableDesc);
			}
		admin.close();
	}
	  public static void create(String tablename,String columnFamily) throws Exception {
	        @SuppressWarnings({ "resource", "deprecation" })
			HBaseAdmin admin = new HBaseAdmin(cfg);
	        if (admin.tableExists(Bytes.toBytes(tablename))) {
	            System.out.println("table Exists!");
	        }
	        else{
	        	System.out.println("create table . . .");
	            @SuppressWarnings("deprecation")
				HTableDescriptor tableDesc = new HTableDescriptor(tablename);
	            tableDesc.addFamily(new HColumnDescriptor(columnFamily));
	            admin.createTable(tableDesc);
	            if (admin.tableExists(Bytes.toBytes(tablename))) {
	            	System.out.println("create table success!");
		        }
	            
	        }
	    }
	  

	/**
	 * 
	 * @param tablename 表名
	 * @param row 列名
	 * @param columnFamily cF
	 * @param column 数据名
	 * @param data 数据
	 * @throws Exception
	 */
	@SuppressWarnings("deprecation")
	public static void put(String tablename, String row, String columnFamily, String column, String data)
			throws Exception {
		@SuppressWarnings({ "resource" })
		HTable table = new HTable(cfg, tablename);
		Put p1 = new Put(Bytes.toBytes(row));
		p1.add(Bytes.toBytes(columnFamily), Bytes.toBytes(column), Bytes.toBytes(data));
		table.put(p1);
		System.out.println("put '" + row + "','" + columnFamily + ":" + column + "','" + data + "'");
	}
	/**
	 * 
	 * @param tablename
	 * @param row
	 * @param columnFamily
	 * @param array mapper值对键
	 * @return put 
	 * @throws Exception
	 */
	@SuppressWarnings("deprecation")
	public static Put put(String tablename, String row,String columnFamily,HashMap<String,String> array )
			throws Exception {
		Put p1 = new Put(Bytes.toBytes(row));
		Set<String> keyset = array.keySet();
		for(Iterator<String> it = keyset.iterator() ; it.hasNext() ;)
		{
			String key = it.next();
		if(array.get(key) != null)
		{
		p1.add(Bytes.toBytes(columnFamily), Bytes.toBytes(key), Bytes.toBytes(array.get(key)));
		}
		else
		{
		System.out.println("好險，避免了一次空指針");	
		}
		}
		
		return p1;
		
	}

	public static void get(String tablename, String row) throws IOException {
		@SuppressWarnings({ "deprecation", "resource" })
		HTable table = new HTable(cfg, tablename);
		Get g = new Get(Bytes.toBytes(row));
		Result result = table.get(g);
		System.out.println("Get: " + result);
	}


	public static void scan(String tablename) throws Exception {
		@SuppressWarnings({ "deprecation", "resource" })
		HTable table = new HTable(cfg, tablename);
		Scan s = new Scan();
		ResultScanner rs = table.getScanner(s);
		for (Result r : rs) {
			System.out.println("Scan: " + r);
		}
	}

	public static boolean delete(String tablename) throws IOException {

		@SuppressWarnings({ "deprecation", "resource" })
		HBaseAdmin admin = new HBaseAdmin(cfg);
		if (admin.tableExists(tablename)) {
			try {
				admin.disableTable(tablename);
				admin.deleteTable(tablename);
			} catch (Exception ex) {
				ex.printStackTrace();
				return false;
			}

		}
		return true;
	}
	
/**
 * 
 * @param d 日期
 * @param m 增加的天数
 * @return 增加完天数后的日期
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
	
		
		// 获取城市的ID JSON文件
		JSONObject cityNumObject = new JSONObject(Read.readJson(cityNumPath));
		
		JSONObject timesetting = new JSONObject(Read.readJson(timesetpath));
		//参数初始化
		 SimpleDateFormat df = new SimpleDateFormat("yyyy-mm-dd");
		Date start = df.parse(timesetting.getString("start-time"));
		Date end =  df.parse(timesetting.getString("end-time"));
		int days = (int)((end.getTime() - start.getTime())/86400000) + 1;
		System.out.println("开始进行" + days+ "天的数据写入. . . ");
		// 开始进行hbase读写
		for(int iter = 0 ; iter < days ; iter ++)
		{
		long start_oneday_time = new Date().getTime();
		String smbstring = "smb://biggrab:123456@192.168.1.111/biggrab/export/"+dateplus(start,iter)+"/";
		SmbFile fs = new SmbFile(smbstring);
		List<String> filestatus = GetFileStatus.showAllFiles(fs);
		for (int i = 0; i < filestatus.size(); i++) {
			File f = GetFileStatus.Save_smb(filestatus.get(i), tmpfilepath);
			String tablename = "city_"+(cityNumObject.getString(((f.getName()).split("\\."))[0]));
			System.out.print("城市名:"+((f.getName()).split("\\."))[0]);
			System.out.println("表名:"+cityNumObject.getString(((f.getName()).split("\\."))[0]));				
			try {
				Test.create(tablename,columnFamily);
				HTable cityTable = new HTable(cfg,tablename);
				JSONArray inputjson = Read.read_jsonFile(f,"utf-8");
				ArrayList<Put> putDateList = new ArrayList<Put>();
				for(int rownum = 0 ; rownum < inputjson.length() ; rownum ++)
				{		
				HashMap<String,String> array = SinaJsonRead.getJsonData(inputjson.getJSONObject(rownum));								
				String UserID = getUserID(array.get("user"));
				String WeiboID = array.get("idstr");
				String rowname = dateplus(start,iter) + "_" + UserID + "_" + WeiboID;	
				Put p1 = new Put(Bytes.toBytes(rowname));
				//插入流写入
//				p1.add(Bytes.toBytes(columnFamily), Bytes.toBytes("idstr"), Bytes.toBytes(array.get("idstr")));
//				p1.add(Bytes.toBytes(columnFamily), Bytes.toBytes("isLongText"), Bytes.toBytes(array.get("isLongText")));
//				p1.add(Bytes.toBytes(columnFamily), Bytes.toBytes("hot_weibo_tags"), Bytes.toBytes(array.get("hot_weibo_tags")));
//				p1.add(Bytes.toBytes(columnFamily), Bytes.toBytes("text_tag_tips"), Bytes.toBytes(array.get("text_tag_tips")));
//				p1.add(Bytes.toBytes(columnFamily), Bytes.toBytes("distance"), Bytes.toBytes(array.get("distance")));
//				p1.add(Bytes.toBytes(columnFamily), Bytes.toBytes("in_reply_to_status_id"), Bytes.toBytes(array.get("in_reply_to_status_id")));
//				p1.add(Bytes.toBytes(columnFamily), Bytes.toBytes("pic_ids"), Bytes.toBytes(array.get("pic_ids")));
//				p1.add(Bytes.toBytes(columnFamily), Bytes.toBytes("created_at"), Bytes.toBytes(array.get("created_at")));
//				p1.add(Bytes.toBytes(columnFamily), Bytes.toBytes("mid"), Bytes.toBytes(array.get("idstr")));
//				p1.add(Bytes.toBytes(columnFamily), Bytes.toBytes("annotations"), Bytes.toBytes(array.get("annotations")));
//				p1.add(Bytes.toBytes(columnFamily), Bytes.toBytes("source"), Bytes.toBytes(array.get("source")));
//				p1.add(Bytes.toBytes(columnFamily), Bytes.toBytes("attitudes_count"), Bytes.toBytes(array.get("attitudes_count")));
//				p1.add(Bytes.toBytes(columnFamily), Bytes.toBytes("rid"), Bytes.toBytes(array.get("rid")));
//				p1.add(Bytes.toBytes(columnFamily), Bytes.toBytes("bmiddle_pic"), Bytes.toBytes(array.get("bmiddle_pic")));
//				p1.add(Bytes.toBytes(columnFamily), Bytes.toBytes("geo"), Bytes.toBytes(array.get("geo")));
//				p1.add(Bytes.toBytes(columnFamily), Bytes.toBytes("darwin_tags"), Bytes.toBytes(array.get("darwin_tags")));
//				p1.add(Bytes.toBytes(columnFamily), Bytes.toBytes("in_reply_to_screen_name"), Bytes.toBytes(array.get("in_reply_to_screen_name")));
//				p1.add(Bytes.toBytes(columnFamily), Bytes.toBytes("mlevel"), Bytes.toBytes(array.get("mlevel")));
//				p1.add(Bytes.toBytes(columnFamily), Bytes.toBytes("in_reply_to_user_id"), Bytes.toBytes(array.get("in_reply_to_user_id")));
//				p1.add(Bytes.toBytes(columnFamily), Bytes.toBytes("id"), Bytes.toBytes(array.get("id")));
//				p1.add(Bytes.toBytes(columnFamily), Bytes.toBytes("text"), Bytes.toBytes(array.get("text")));
//				p1.add(Bytes.toBytes(columnFamily), Bytes.toBytes("reposts_count"), Bytes.toBytes(array.get("reposts_count")));
//				p1.add(Bytes.toBytes(columnFamily), Bytes.toBytes("favorited"), Bytes.toBytes(array.get("favorited")));
//				p1.add(Bytes.toBytes(columnFamily), Bytes.toBytes("visible"), Bytes.toBytes(array.get("visible")));
//				p1.add(Bytes.toBytes(columnFamily), Bytes.toBytes("thumbnail_pic"), Bytes.toBytes(array.get("thumbnail_pic")));
//				p1.add(Bytes.toBytes(columnFamily), Bytes.toBytes("original_pic"), Bytes.toBytes(array.get("original_pic")));
//				p1.add(Bytes.toBytes(columnFamily), Bytes.toBytes("textLength"), Bytes.toBytes(array.get("textLength")));
//				p1.add(Bytes.toBytes(columnFamily), Bytes.toBytes("truncated"), Bytes.toBytes(array.get("truncated")));
//				p1.add(Bytes.toBytes(columnFamily), Bytes.toBytes("source_type"), Bytes.toBytes(array.get("source_type")));
//				p1.add(Bytes.toBytes(columnFamily), Bytes.toBytes("biz_feature"), Bytes.toBytes(array.get("biz_feature")));
//				p1.add(Bytes.toBytes(columnFamily), Bytes.toBytes("source_allowclick"), Bytes.toBytes(array.get("source_allowclick")));
//				p1.add(Bytes.toBytes(columnFamily), Bytes.toBytes("comments_count"), Bytes.toBytes(array.get("comments_count")));
//				p1.add(Bytes.toBytes(columnFamily), Bytes.toBytes("url_objects"), Bytes.toBytes(array.get("url_objects")));
//				p1.add(Bytes.toBytes(columnFamily), Bytes.toBytes("userType"), Bytes.toBytes(array.get("userType")));
//				p1.add(Bytes.toBytes(columnFamily), Bytes.toBytes("user"), Bytes.toBytes(array.get("user")));
//				p1.add(Bytes.toBytes(columnFamily), Bytes.toBytes("biz_ids"), Bytes.toBytes(array.get("biz_ids")));
				p1 = Test.put(tablename, rowname, columnFamily, array);
				putDateList.add(p1);
				if (putDateList.size() > 1000){
					cityTable.put(putDateList);
					cityTable.flushCommits();
					putDateList.clear();
					System.out.println("进行一次写入");
					}	
				}
				cityTable.put(putDateList);
				cityTable.flushCommits();
				putDateList.clear();
				System.out.println("结尾处进行一次写入");
				cityTable.close();
			}

			catch (Exception e) {
				e.printStackTrace();
			
			}
		
	
		}
		//完成一天的写入
		String times = Integer.toString(iter);
		long end_oneday_time = new Date().getTime();
		long use_time = (end_oneday_time - start_oneday_time)/(1000*60*60);
		try {
			dateInsert((times+"_"+use_time), dateplus(start,iter));
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}
	}

	@SuppressWarnings("unused")
	private static void dateInsert(String iter,String date) throws JSONException, IOException {
		// TODO Auto-generated method stub
		JSONObject cd = new JSONObject();
		cd.append("completedate_" + iter,date );
		File f = new File(completeddate);
	    if(!f.exists()){
	        f.createNewFile();
	       }
	    FileWriter fileWritter = new FileWriter(f.getName(),true);
        BufferedWriter bufferWritter = new BufferedWriter(fileWritter);
        bufferWritter.write(cd.toString());
        bufferWritter.close();

    System.out.println("Done One day");
	}
	private static String getUserID(String JsonString) throws JSONException {
		// TODO Auto-generated method stub
		JSONObject j = new JSONObject(JsonString);
		String gender = j.getString("gender");
		String id = j.getString("id");
		String renturnstring = gender + "_" +  id;
		return renturnstring;
	}
}
