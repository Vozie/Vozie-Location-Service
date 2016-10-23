package vozie.ridesharing.app;

public class Driver {
	private String id, latitude, longitude;
	private boolean active = false;
	
	public Driver (String id) {
		this.id = id;
	}
	
	public void setLatitude(String latitude) {
		this.latitude = latitude;
	}
	
	public void setLongitude(String longitude) {
		this.longitude = longitude;
	}
	
	public String getLatitude() {
		return this.latitude;
	}
	
	public String getLongitude() {
		return this.longitude;
	}
	
	public String getId() {
		return this.id;
	}
	
	public boolean getState() {
		return active;
	}
	
	public void setState(boolean active) {
		this.active = active;
	}
}
