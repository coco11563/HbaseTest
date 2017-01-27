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
import org.apache.hadoop.hbase.util.Threads;
import org.apache.log4j.Logger;

import net.sf.json.JSONArray;
import net.sf.json.JSONException;
import net.sf.json.JSONObject;
import jcifs.smb.*;
import org.jruby.RubyProcess;

public class Test {
	//static变量初始化
	private final static String tmpfilepath = "./tmp";
	private final static String cityNumPath = "./conf/cityNum.json";
	private final static String timesetpath = "./conf/timeSetting.json";
	private final static String completeddate = "./conf/CompletedDate.json";
	private final static String completednum = "./conf/completednum.json";
	
	// 获取HBaseConfiguration
	static Configuration cfg = HBaseConfiguration.create();
	
	private static final String columnFamily = "sinadata";
	
	//log4j initial
	
	private static Logger logger = Logger.getLogger(Test.class);  
	
	// HBaseAdmin HTableDescriptor
	@SuppressWarnings("deprecation")
	public static void create(String tableName)throws IOException {
		logger.info("正在创建"+tableName);
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
	        System.out.print(cfg);
	        if (admin.tableExists(Bytes.toBytes(tablename))) {
	        	logger.error("table Exists!");
	        }
	        else{
	        	logger.info("create table . . .");
	            @SuppressWarnings("deprecation")
				HTableDescriptor tableDesc = new HTableDescriptor(tablename);
	            tableDesc.addFamily(new HColumnDescriptor(columnFamily));
	            admin.createTable(tableDesc);
	            if (admin.tableExists(Bytes.toBytes(tablename))) {
	            	logger.info("create table success!");
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
		logger.info("put '" + row + "','" + columnFamily + ":" + column + "','" + data + "'");
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
		for(Iterator<String> it = keyset.iterator() ; it.hasNext() ;)//使用了一个很棒的办法遍历了map
		{
			String key = it.next();
		if(array.get(key) != null)
		{
		p1.add(Bytes.toBytes(columnFamily), Bytes.toBytes(key), Bytes.toBytes(array.get(key)));
		}
		else
		{
			logger.info("好險，避免了一次空指針");	
		}
		}
		
		return p1;
		
	}

	public static void get(String tablename, String row) throws IOException {
		@SuppressWarnings({ "deprecation", "resource" })
		HTable table = new HTable(cfg, tablename);
		Get g = new Get(Bytes.toBytes(row));
		Result result = table.get(g);
		logger.info("Get: " + result);
	}


	public static void scan(String tablename) throws Exception {
		@SuppressWarnings({ "deprecation", "resource" })
		HTable table = new HTable(cfg, tablename);
		Scan s = new Scan();
		ResultScanner rs = table.getScanner(s);
		for (Result r : rs) {
			logger.info("Scan: " + r);
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
		JSONObject cityNumObject = JSONObject.fromObject(Read.readJson(cityNumPath));
		
		JSONObject timesetting = JSONObject.fromObject(Read.readJson(timesetpath));
		//参数初始化
		 SimpleDateFormat df = new SimpleDateFormat("yyyy-mm-dd");
		Date start = df.parse(timesetting.getString("start-time"));
		Date end =  df.parse(timesetting.getString("end-time"));
		int days = (int)((end.getTime() - start.getTime())/86400000) + 1;
		logger.info("开始进行" + days+ "天的数据写入. . . ");
		// 开始进行hbase读写
		JSONArray inputjson = new JSONArray();
		for(int iter = 0 ; iter < days ; iter ++)//天数
		{
		int stornum = 0; //存储写入数量
		long start_oneday_time = new Date().getTime();
		String smbstring = "smb://biggrab:123456@192.168.1.111/biggrab/export/"+dateplus(start,iter)+"/";
		String smbzipstring = "smb://biggrab:123456@192.168.1.111/biggrab/export/"+dateplus(start,iter)+".zip";
		SmbFile fs = new SmbFile(smbstring);
		List<String> filestatus = GetFileStatus.showAllFiles(fs);

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
			for (int i = 0; i < filestatus.size(); i++) {//按城市遍历
				String tablename = "city_";
			//File f = GetFileStatus.Save_smb(filestatus.get(i), tmpfilepath);
			//String tablename = "city_"+(cityNumObject.getString(((f.getName()).split("\\."))[0]));
			//logger.info("城市名:"+((f.getName()).split("\\."))[0]);
			//logger.info("表名:"+cityNumObject.getString(((f.getName()).split("\\."))[0]));
				if(filestatus.get(i).split("/").length == 8) {
					tablename = tablename + (cityNumObject.getString((filestatus.get(i).split("/")[7]).split("\\.")[0]));
					logger.info("城市名:" + ((filestatus.get(i).split("/"))[7]).split("\\.")[0]);
					logger.info("表名:" + tablename);
				}
				else
				{
					tablename = tablename + (cityNumObject.getString((filestatus.get(i).split("/")[6]).split("\\.")[0]));
					logger.info("城市名:" + ((filestatus.get(i).split("/"))[6]).split("\\.")[0]);
					logger.info("表名:" + tablename);
				}
                try {
					SmbFile remotefs = new SmbFile(filestatus.get(i));
				inputjson = Read.read_jsonFile(remotefs,"utf-8");
					stornum += inputjson.size() ;
				Test.create(tablename,columnFamily);
				HTable cityTable = new HTable(cfg,tablename);
				ArrayList<Put> putDateList = new ArrayList<Put>();
				for(int rownum = 0 ; rownum < inputjson.size() ; rownum ++)//按行数遍历
				{		
				HashMap<String,String> array = SinaJsonRead.getJsonData(inputjson.getJSONObject(rownum));								
				String UserID = getUserID(array.get("user"));
				String WeiboID = array.get("idstr");
				String rowname = dateplus(start,iter) + "_" + UserID + "_" + WeiboID;	
				Put p1 = new Put(Bytes.toBytes(rowname));
				//插入流写入
				p1 = Test.put(tablename, rowname, columnFamily, array);
				putDateList.add(p1);
				if (putDateList.size() > 1000){
					cityTable.put(putDateList);
					cityTable.flushCommits();
					putDateList.clear();
					logger.info("进行一次写入");
					}	
				}
				cityTable.put(putDateList);
				cityTable.flushCommits();
				putDateList.clear();
				logger.info("结尾处进行一次写入");
					inputjson = null ;
				cityTable.close();

			}

			catch (Exception e) {
				e.printStackTrace();
			
			}
			
		}
		
		
		//完成一天的写入
		String times = Integer.toString(iter);
		long end_oneday_time = new Date().getTime();
		long use_time = (end_oneday_time - start_oneday_time)/(1000*60);//分钟存储运行时间
		try {
			numInsert(dateplus(start,iter),stornum) ;
			dateInsert((times+"_"+use_time), dateplus(start,iter));
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		stornum = 0;
		//开始压缩作业
		logger.info("start to zip the file");
		ZipUtils.createSmbZip(smbstring,smbzipstring);
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
