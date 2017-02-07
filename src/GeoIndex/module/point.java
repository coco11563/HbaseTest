package GeoIndex.module;
public class point {
				private int num = 0;
				private int lastgrab = 0;
				private int pointType = 0;
				private double lat ;
				private double lng ;
				private String province ;
				private String city ;
				/**
				 * 
				 * @param lat
				 * @param lng
				 * @param pointType
				 * @param lastgrab
				 * @param num
				 */
				public void set(double lat,double lng , int pointType , int lastgrab , int num)
				{
					this.lastgrab = lastgrab;
					this.lat = lat;
					this.lng = lng;
					this.num = num;
					this.pointType = pointType;
				}
				public int getPointType() {
					return pointType;
				}
				public void setPointType(int pointType) {
					this.pointType = pointType;
				}
				public double getLat() {
					return lat;
				}
				public void setLat(double lat) {
					this.lat = lat;
				}
				public double getLng() {
					return lng;
				}
				public void setLng(double lng) {
					this.lng = lng;
				}
				public int getNum() {
					return num;
				}
				public void setNum(int num) {
					this.num = num;
				}
				public int getLastgrab() {
					return lastgrab;
				}
				public void setLastgrab(int lastgrab) {
					this.lastgrab = lastgrab;
				}
				public String getProvince() {
					return province;
				}
				public void setProvince(String province) {
					this.province = province;
				}
				public String getCity() {
					return city;
				}
				public void setCity(String city) {
					this.city = city;
				}
				
				
}
