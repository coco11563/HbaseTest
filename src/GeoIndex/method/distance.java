package GeoIndex.method;

import GeoIndex.module.point;

import java.util.Date;

public class distance {
public double distances(point p1 , point p2)
{
	double lat1 = p1.getLat();
	double lat2 = p2.getLat();
	double lng1 = p1.getLng();
	double lng2 = p2.getLng();
	double distance = caculate(lat1,lat2,lng1,lng2);
	return distance;
	}
public static double caculate(double lat1 , double lat2 , double lng1,double lng2)
{
	 double a, b, R;  
	    R = 6378137;
	    lat1 = lat1 * Math.PI / 180.0;  
	    lat2 = lat2 * Math.PI / 180.0;  
	    a = lat1 - lat2;  
	    b = (lng1 - lng2) * Math.PI / 180.0;  
	    double d;  
	    double sa2, sb2;  
	    sa2 = Math.sin(a / 2.0);  
	    sb2 = Math.sin(b / 2.0);  
	    d = 2  
	            * R  
	            * Math.asin(Math.sqrt(sa2 * sa2 + Math.cos(lat1)  
	                    * Math.cos(lat2) * sb2 * sb2));  
	    return d;  
	}
public static void main(String args[])
{
	Date date = new Date();   
	long start = date.getTime();
	for(int i = 1 ; i < 1000000 ; i ++)
	{
	double d = caculate(13,24,44,33);
	System.out.println(i);
	}
	Date date2 = new Date();  
	long end = date2.getTime();
	System.out.print((end - start)/1000 + "'s");
}
}
