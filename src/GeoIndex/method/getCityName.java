package GeoIndex.method;

import net.sf.json.JSONArray;
import net.sf.json.JSONException;
import net.sf.json.JSONObject;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;

public class getCityName {
	/**
	 * 用来调用url
	 * @param url
	 * @return String from web
	 */
	private static String pcn_connUrl(String url){
		URL getUrl;
		BufferedReader br = null;
		StringBuffer buffer = new StringBuffer() ;
		try 
		{
			getUrl = new URL(url);
			HttpURLConnection connection = (HttpURLConnection) getUrl.openConnection();
			connection.setConnectTimeout(30*1000);
			connection.connect();
			InputStreamReader isr = new InputStreamReader(connection.getInputStream(),"utf-8");
			br = new BufferedReader(isr); 
			int s;
			while((s = br.read())!=-1)
			{
				buffer.append((char)s);
			}
		}
		catch (IOException e) 
		{
			System.out.println ("error" );
		}
		return  buffer.toString() ;	
	}
	/**
	 * 从json中解析出坐标
	 * 然后根据这个坐标调用百度API返回城市名和省市名
	 * @param jsonObject
	 * @return String[] 省市名
	 * @throws IOException
	 */
	private static String[] pcn_getProCityNameURL(JSONObject jsonObject) throws  IOException
	{
		String[]			cityName			=		null;
		try
		{
			double 				location[] 			= 		pcn_getCoordinates( jsonObject ) ;	
//			String 				url						=		"http://api.map.baidu.com/geocoder?location="+location[0]+","+location[1]+"&output=json&key=28bcdd84fae25699606ffad27f8da77b" ;
			String  			url						=  		"http://api.map.baidu.com/geocoder/v2/?ak=Q9D8ftvpm5PRFvAK4gkM4HguKuVRXCHe&location="+location[0]+","+location[1]+"&output=json&coordtype=gcj02ll";
			String 				baiduLocation 	= 		pcn_connUrl(url) ;
			JSONObject 		temp_object		=		JSONObject.fromObject(baiduLocation) ;
			temp_object		= 		temp_object.getJSONObject("result") ;
			temp_object		= 		temp_object.getJSONObject("addressComponent") ;
			cityName			=		new String[2] ;
			cityName[0]		=		temp_object.getString("province") ;
			cityName[1]		=		temp_object.getString("city") ;
			
			if(cityName[1].contains ( "直辖县级行政单位" ) )
				cityName[1]	=		temp_object.getString("district") ;
			
			System.out.println ( location[0]+"\t"+location[1] + ":\t" + cityName[0]+"-"+cityName[1]);
			return cityName;
		}
		catch(Exception e)
		{
			cityName		=		new String[2] ;
			cityName[0]	=	"Exception" ;
			cityName[1]	=	"Exception" ;
			return cityName;
		}
	}
	/**
	 * 输入坐标，返回省市名
	 * @param lat
	 * @param lng
	 * @return 省市名
	 * @throws IOException
	 */
	public static String[] pcn_getProCityNameURL(double lat , double lng) throws  IOException
	{
		String[]			cityName			=		null;
		double 				location[] 			= 		{lat,lng} ;	
		try
		{
		
//			String 				url						=		"http://api.map.baidu.com/geocoder?location="+location[0]+","+location[1]+"&output=json&key=28bcdd84fae25699606ffad27f8da77b" ;
			String  			url						=  		"http://api.map.baidu.com/geocoder/v2/?ak=yp6K3a5ISOu5OsZtGzwkH8SHe1KRZa5Q&location="+location[0]+","+location[1]+"&output=json&coordtype=gcj02ll";
			String 				baiduLocation 	= 		pcn_connUrl(url) ;
			JSONObject 		temp_object		=		JSONObject.fromObject(baiduLocation) ;
			temp_object		= 		temp_object.getJSONObject("result") ;
			temp_object		= 		temp_object.getJSONObject("addressComponent") ;
			cityName			=		new String[2] ;
			cityName[0]		=		temp_object.getString("province") ;
			cityName[1]		=		temp_object.getString("city") ;
			
			if(cityName[1].contains ( "直辖县级行政单位" ) )
				cityName[1]	=		temp_object.getString("district") ;
			
			System.out.println ( location[0]+"\t"+location[1] + ":\t" + cityName[0]+"-"+cityName[1]);
			return cityName;
		}
		catch(Exception e)
		{
			cityName		=		new String[2] ;
			cityName[0]	=	"Exception" ;
			cityName[1]	=	"Exception" ;
			System.out.println ( location[0]+"\t"+location[1] + ":\t" + cityName[0]+"-"+cityName[1]);
			return cityName;
		}
	}
	/**
	 * 负责从输入的jsonobj中解析出地理坐标
	 * @param json_object
	 * @return 经纬度坐标
	 * @throws JSONException
	 */
	private static double[] pcn_getCoordinates(JSONObject json_object) throws JSONException
	{
		JSONObject 	geo_object 		= 		json_object.getJSONObject ( "geo" ) ;
		JSONArray coor_jsarray		=		geo_object.getJSONArray ( "coordinates" ) ;
		double 			location[] 			= 		new double[2] ;
		location[0]		=		coor_jsarray.getDouble ( 0 ) ;
		location[1]		=		coor_jsarray.getDouble ( 1 ) ;
		return location ;
	}
	
	public  static void main(String args[]) throws IOException
	{
		String[]			cityName			=		null;
		cityName = pcn_getProCityNameURL(36.400000000000055,114.4000000000001) ;
		System.out.println(cityName[0]);
	}
	
	
}
