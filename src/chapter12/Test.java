package chapter12;

import java.io.File;
import java.io.IOException;
import java.net.MalformedURLException;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.LinkedHashMap;
import java.util.List;

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
	//配置文件地址
	private final static String tmpfilepath = "D:\\库\\文档\\eclipse workspace\\HbaseTest\\tmp";
	private final static String cityNumPath = "D:\\库\\文档\\eclipse workspace\\HbaseTest\\conf\\cityNum.json";
	private final static String timesetpath = "D:\\库\\文档\\eclipse workspace\\HbaseTest\\conf\\timeSetting.json";
	// 声明静态配置 HBaseConfiguration及一些常量
	static Configuration cfg = HBaseConfiguration.create();
	private static final String columnFamily = "sinadata";
	// 创建一张表，通过HBaseAdmin HTableDescriptor来创建
	public static void create(String tableName)throws IOException {
		System.out.println("创建表"+tableName);
		HBaseAdmin admin = new HBaseAdmin(cfg);
		if (!admin.tableExists(tableName)) {
			@SuppressWarnings("deprecation")
			HTableDescriptor tableDesc = new HTableDescriptor(tableName);
			tableDesc.addFamily(new HColumnDescriptor(columnFamily));
			admin.createTable(tableDesc);
			}
		admin.close();
	}
	  public static void creat(String tablename,String columnFamily) throws Exception {
	        @SuppressWarnings({ "resource", "deprecation" })
			HBaseAdmin admin = new HBaseAdmin(cfg);
	        if (admin.tableExists(tablename)) {
	            System.out.println("table Exists!");
	            System.exit(0);
	        }
	        else{
	            @SuppressWarnings("deprecation")
				HTableDescriptor tableDesc = new HTableDescriptor(tablename);
	            tableDesc.addFamily(new HColumnDescriptor(columnFamily));
	            admin.createTable(tableDesc);
	            System.out.println("create table success!");
	        }
	    }
	  
	// 添加一条数据，通过HTable Put为已经存在的表来添加数据
	/**
	 * 
	 * @param tablename 表名
	 * @param row 列明
	 * @param columnFamily cF
	 * @param column 列中数据名
	 * @param data 数值
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
	 * @param array mapper集合
	 * @throws Exception
	 */
	public static void put(String tablename, String row,String columnFamily,LinkedHashMap<String,String> array )
			throws Exception {
		@SuppressWarnings({ "resource" })
		HTable table = new HTable(cfg, tablename);
		Put p1 = new Put(Bytes.toBytes(row));
		p1.add(Bytes.toBytes(columnFamily), Bytes.toBytes("idstr"), Bytes.toBytes(array.get("idstr")));
		table.put(p1);
		
	}

	public static void get(String tablename, String row) throws IOException {
		@SuppressWarnings({ "deprecation", "resource" })
		HTable table = new HTable(cfg, tablename);
		Get g = new Get(Bytes.toBytes(row));
		Result result = table.get(g);
		System.out.println("Get: " + result);
	}

	// 显示所有数据，通过HTable Scan来获取已有表的信息
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
 * @param d 输入日期
 * @param m 增加的天数
 * @return 输入日期加上相应的天数的返回时间
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
	public static void main(String[] agrs) throws MalformedURLException, JSONException, SmbException, ParseException
	{
	
		
		// 读取城市ID JSON文件
		JSONObject cityNumObject = new JSONObject(Read.readJson(cityNumPath));
		JSONObject timesetting = new JSONObject(Read.readJson(timesetpath));
		//获取本次起止时间以及循环时间
		 SimpleDateFormat df = new SimpleDateFormat("yyyy-mm-dd");
		Date start = df.parse(timesetting.getString("start-time"));
		Date end =  df.parse(timesetting.getString("end-time"));
		int days = (int)((end.getTime() - start.getTime())/86400000) + 1;
			System.out.println(days);
		// 获取第一个目录下全部文件ID
		for(int iter = 0 ; iter < days ; iter ++)
		{
		
		String smbstring = "smb://biggrab:123456@192.168.1.111/biggrab/export/"+dateplus(start,iter)+"/";
		System.out.println(smbstring);
		SmbFile fs = new SmbFile(smbstring);
		List<String> filestatus = GetFileStatus.showAllFiles(fs);
		for (int i = 0; i < filestatus.size(); i++) {
			File f = GetFileStatus.Save_smb(filestatus.get(i), tmpfilepath);
			String tablename = (cityNumObject.getString(f.getName()).split("\\.")[0]);
			
				
			
			try {
				Test.create(tablename);
				JSONArray inputjson = Read.read_jsonFile(f,"utf-8");
				for(int rownum = 0 ; rownum < inputjson.length() ; rownum ++)
				{		
				LinkedHashMap<String,String> array = SinaJsonRead.getJsonData(inputjson.getJSONObject(rownum));								
				String UserID = getUserID(array.get("user"));
				String WeiboID = array.get("idstr");
				String rowname = dateplus(start,iter) + "_" + UserID + "_" + WeiboID;	
				Test.put(tablename, rowname, columnFamily,array);
				
					}
			}

			catch (Exception e) {
				e.printStackTrace();
			
			}
		
	
		}
	}
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
