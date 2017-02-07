package GeoIndex.main;


public class AreaData {
	private double lat_min;//维度
	private double lon_min;//经度
	private double lat_max;
	private double lon_max;
    public AreaData(double la_min,double la_max,double lo_min,double lo_max){
		lat_min = la_min;
		lon_min = lo_min;
		lat_max = la_max;
		lon_max = lo_max;
	}
	public double getLat_min() {
		return lat_min;
	}
	public void setLat_min(double lat_min) {
		this.lat_min = lat_min;
	}
	public double getLon_min() {
		return lon_min;
	}
	public void setLon_min(double lon_min) {
		this.lon_min = lon_min;
	}
	public double getLat_max() {
		return lat_max;
	}
	public void setLat_max(double lat_max) {
		this.lat_max = lat_max;
	}
	public double getLon_max() {
		return lon_max;
	}
	public void setLon_max(double lon_max) {
		this.lon_max = lon_max;
	}
}
