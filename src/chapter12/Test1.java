package chapter12;

import java.io.BufferedWriter;
import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.util.Date;
import java.util.LinkedHashMap;

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
import org.apache.hadoop.hbase.filter.CompareFilter;
import org.apache.hadoop.hbase.filter.Filter;
import org.apache.hadoop.hbase.filter.RegexStringComparator;
import org.apache.hadoop.hbase.filter.RowFilter;
import org.apache.hadoop.hbase.protobuf.generated.HBaseProtos.CompareType;
import org.apache.hadoop.hbase.util.Bytes;

import net.sf.json.JSONException;
import net.sf.json.JSONObject;

public class Test1 {

    static Configuration cfg=HBaseConfiguration.create();


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
  
    //���һ�����ݣ�ͨ��HTable PutΪ�Ѿ����ڵı����������
    @SuppressWarnings("deprecation")
	public static void put(String tablename,String row, String columnFamily,String column,String data) throws Exception {
        @SuppressWarnings({ "resource" })
		HTable table = new HTable(cfg, tablename);
        Put p1=new Put(Bytes.toBytes(row));
        p1.add(Bytes.toBytes(columnFamily), Bytes.toBytes(column), Bytes.toBytes(data));
        table.put(p1);
        System.out.println("put '"+row+"','"+columnFamily+":"+column+"','"+data+"'");
    }
   
   public static void get(String tablename,String row) throws IOException{
            @SuppressWarnings({ "deprecation", "resource" })
			HTable table=new HTable(cfg,tablename);
            Get g=new Get(Bytes.toBytes(row));
                Result result=table.get(g);
                System.out.println("Get: "+result);
    }
    //��ʾ�������ݣ�ͨ��HTable Scan����ȡ���б����Ϣ
    public static void scan(String tablename) throws Exception{
         @SuppressWarnings({ "deprecation", "resource" })
		HTable table = new HTable(cfg, tablename);
         Scan s = new Scan();
         ResultScanner rs = table.getScanner(s);
         for(Result r:rs){
             System.out.println("Scan: "+r);
         }
    }
    
    public static boolean delete(String tablename) throws IOException{
            
            @SuppressWarnings({ "deprecation", "resource" })
			HBaseAdmin admin=new HBaseAdmin(cfg);
            if(admin.tableExists(tablename)){
                    try
                    {
                            admin.disableTable(tablename);
                            admin.deleteTable(tablename);
                    }catch(Exception ex){
                            ex.printStackTrace();
                            return false;
                    }
                    
            }
            return true;
    }
	private static void dateInsert(String iter,String date) throws JSONException, IOException {
		// TODO Auto-generated method stub
		
		JSONObject cd = new JSONObject();
		cd.element("completedate_" + iter,date );
		File f = new File("./conf/CompletedDate.json");
	    if(!f.exists()){
	        f.createNewFile();
	       }
	    FileWriter fileWritter = new FileWriter(f.getName(),true);
	    System.out.println(f.getAbsolutePath());
        BufferedWriter bufferWritter = new BufferedWriter(fileWritter);
        bufferWritter.write(cd.toString());
        bufferWritter.close();

    System.out.println("Done");
	}
	public static Put put(String tablename, String row,String columnFamily,LinkedHashMap<String,String> array )
			throws Exception {
		@SuppressWarnings({ "resource" })
		HTable table = new HTable(cfg, tablename);
		Put p1 = new Put(Bytes.toBytes(row));
		String[] keyset = (String[]) array.keySet().toArray();
		for(int i = 0 ; i < keyset.length ; i++)
		{
		if(array.get(keyset[i]) != null)
		p1.add(Bytes.toBytes(columnFamily), Bytes.toBytes(keyset[i]), Bytes.toBytes(array.get(keyset[i])));
		}
		return p1;
		
	}
    public static void  main(String [] agrs) throws IOException, JSONException {
//    Test1.put(tablename, row, columnFamily, array)
    	
    	
    	//String tablename="hbase_tb";
//            String columnFamily="cf";
//          
//          try {                     
//        	     System.out.println("Delete table:"+tablename+"success!");
//            Test1.put(tablename, "row1", columnFamily, "cl1", "data");
//            Test1.get(tablename, "row1");
//            System.out.println("Delete table:"+tablename+"success!");
//            Test1.scan(tablename);
//           if(true==Test1.delete(tablename))
//                    System.out.println("Delete table:"+tablename+"success!");
//           
//        }
//        catch (Exception e) {
//            e.printStackTrace();
//        }  
    	
    	
    	
//    	Scan scan = new Scan();
//    	Filter filter = new RowFilter(CompareFilter.CompareOp.EQUAL, new RegexStringComparator(".*_m_.*"));
//    	scan.setFilter(filter);
//    	
//    	HTable table = new HTable(cfg, "city_0205");
//    	 Date Start=new Date();    
//    	ResultScanner scanner = table.getScanner(scan);
//    	 Date end=new Date();    
//    	double time=  ((end.getTime()-Start.getTime())/1000);   
//    	System.out.println("花费时间:" + time + "s");
//    	
//    	for(Result res : scanner)
//    		System.out.println(res);
//    	
//    	scanner.close();
    	
    	
}


}