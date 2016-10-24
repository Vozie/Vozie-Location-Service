package vozie.ridesharing.app;

import org.json.*;
import java.net.HttpURLConnection;
import java.net.URL;
import java.io.InputStreamReader;
import java.io.InputStream;
import java.io.DataOutputStream;
import java.io.BufferedReader;
import java.util.*;


public class LocationService {
	private static final String API_KEY = "AIzaSyCzFInTxNdp-3PVnjkGOASWm3lT96QfrDY";
	
	private String command;
	private String arg1, arg2, arg3, arg4;
	private List<Driver> drivers;
	
    public LocationService() {
    	drivers = new ArrayList();
    }
    
    public String getContent(String command, String arg1, String arg2, String arg3, String arg4) {
    	this.command = command;
    	this.arg1 = arg1;
    	this.arg2 = arg2;
    	this.arg3 = arg3;
    	this.arg4 = arg4;
    	
    	switch(command) {
    		/* getEstimatedTripTime
    		 * 
    		 * arg1		Latitude String of starting point
    		 * arg2		Longitude String of starting point
    		 * arg3		Latitude String of destination point
    		 * arg4		Longitude String of destination point		
    		*/
    		case "getEstimatedTripTime":
    			return getTripTime(arg1, arg2, arg3, arg4);
    			
    		/* getEstimatedArrivalTime
    		 * 
    		 * arg1 	Driver ID String
    		 * arg2		User Latitude String
    		 * arg3		User Longitude String
    		 */
    		case "getEstimatedArrivalTime":
    			if (getDriverIndexById(arg1) != -1) {
    				Driver tDriver = drivers.get(getDriverIndexById(arg1));
    				return getTripTime(tDriver.getLatitude(), tDriver.getLongitude(), arg2, arg3);
    			}
    			return "Driver ID not found!";
    			
    		/* setDriverLocation
    		 * 
    		 * arg1		Driver ID String
    		 * arg2		Driver Latitude String
    		 * arg3		Driver Longitude String
    		 */
    			
    		/* addDriver
    		 * 
    		 * arg1		Driver ID String
    		 */
    		case "addDriver":
    			if (addDriver(arg1))
    				return "Added new driver with id: " + arg1;
    			else
    				return "Failed to add driver. ID exists";
    			
			/* setDriverLocation
    		 * 
    		 * arg1		Driver ID String
    		 * arg2		Latitude String
    		 * arg2		Longitude String
    		 */
    		case "setDriverLocation":
    			if (setDriverLatLng(arg1, arg2, arg3))
    				return "Set LatLng";
    			return "Failed to set LatLng";
    			
    		/* getDriverLocation
    		 * 
    		 * arg1		Driver ID String
    		 */
    		case "getDriverLocation":
    			if (getDriverIndexById(arg1) != -1)
    				return drivers.get(getDriverIndexById(arg1)).getLatitude() + "," + drivers.get(getDriverIndexById(arg1)).getLongitude();
    			return "Failed to locate driver with specified ID";
    		
    			
			/* setMaxPickupDistance
    		 * 
    		 * arg1		Driver ID String
    		 * arg2		Max Pickup Distance Integer
    		 */
    		case "setMaxPickupDistance":
    			if (getDriverIndexById(arg1) != -1) {
    				drivers.get(getDriverIndexById(arg1)).setMaxPickupDistance(Integer.valueOf(arg2));
    				return "Set Max Pickup Distance";
    			}
    			return "Failed to locate driver with specified ID";
    			
    			
			/* getDriversWithinRange
    		 * 
    		 * arg1		User Latitude String
    		 * arg2		User Longitude String
    		 */
    		case "getDriversWithinRange":
    			List<Driver> driversInRange = new ArrayList();
    			double userLatitude = Double.valueOf(arg1);
    			double userLongitude = Double.valueOf(arg2);
    			
    			for (int i = 0; i < drivers.size(); i++) {
    				Driver tDriver = drivers.get(i);
    				double driverLatitude = Double.valueOf(tDriver.getLatitude());
    				double driverLongitude = Double.valueOf(tDriver.getLongitude());
    			
    				if (distance(userLatitude, userLongitude, driverLatitude, driverLongitude) <= tDriver.getMaxPickupDistance())
    					driversInRange.add(tDriver);
    			}
    			
    			String retString = "";
    			for (int i = 0; i < driversInRange.size(); i++)
    				retString += driversInRange.get(i).getId() + ",";
    			
    			return retString;
    			
			/* setDriverActive
    		 * 
    		 * arg1		Driver ID to set as active
    		 */
    		case "setDriverActive":
    			drivers.get(getDriverIndexById(arg1)).setState(true);
    			return "Successfully set driver active";
    		
			/* setDriverInactive
    		 * 
    		 * arg1		Driver ID to set as inactive
    		 */
    		case "setDriverInctive":
    			drivers.get(getDriverIndexById(arg1)).setState(false);
    			return "Successfully set driver inactive";
    	}
    	
    	return "";
    }
    
    public static String executePost(String targetURL) {
		HttpURLConnection connection = null;
		
		try {
			//Create connection
			URL url = new URL(targetURL);
			connection = (HttpURLConnection) url.openConnection();
			connection.setRequestMethod("POST");
			connection.setRequestProperty("Content-Type", 
			"application/x-www-form-urlencoded");
			
			connection.setRequestProperty("Content-Language", "en-US");  
			
			connection.setUseCaches(false);
			connection.setDoOutput(true);
			
			//Send request
			DataOutputStream wr = new DataOutputStream (
			    connection.getOutputStream());
			wr.close();
			
			//Get Response  
			InputStream is = connection.getInputStream();
			BufferedReader rd = new BufferedReader(new InputStreamReader(is));
			StringBuilder response = new StringBuilder(); // or StringBuffer if Java version 5+
			String line;
			while ((line = rd.readLine()) != null) {
				response.append(line);
				response.append('\r');
			}
			rd.close();
			return response.toString();
		} catch (Exception e) {
			e.printStackTrace();
			return null;
		} finally {
			if (connection != null) {
				connection.disconnect();
			}
		}
    }
    
    public boolean addDriver(String id) {
    	for (int i = 0; i < drivers.size(); i++)
    		if (drivers.get(i).getId().equals(id))
    			return false;
    	
    	Driver tDriver = new Driver(id);
    	drivers.add(tDriver);
    	return true;
    }
    
    public void activateDriver(String id) {
    	int driverIndex = getDriverIndexById(id);
    	
    	if (driverIndex != -1)
    		drivers.get(driverIndex).setState(true);
    }
    
    public void deactivateDriver(String id) {
    	int driverIndex = getDriverIndexById(id);
    	
    	if (driverIndex != -1)
    		drivers.get(driverIndex).setState(false);
    }
    
    public boolean setDriverLatLng(String id, String latitude, String longitude) {
    	for (int i = 0; i < drivers.size(); i++)
    		if (drivers.get(i).getId().equals(id)) {
    			drivers.get(i).setLatitude(latitude);
    			drivers.get(i).setLongitude(longitude);
    			return true;
    		}
    	return false;
    }
    
    public static String getTripTime(String startLat, String startLong, String endLat, String endLong) {
    	String queryUrl = "https://maps.googleapis.com/maps/api/directions/json?origin=" + startLat + 
    			"," + startLong + "&destination=" + endLat + "," + endLong + "&key=" + API_KEY;
    			JSONObject resultJson = new JSONObject(executePost(queryUrl));
    			JSONArray resultJsonArray = (JSONArray) resultJson.get("routes");
    			JSONObject resultJsonObject1 = (JSONObject) resultJsonArray.getJSONObject(0);
    			JSONArray resultJsonArray2 = (JSONArray) resultJsonObject1.getJSONArray("legs");
    			JSONObject resultJsonObject3 = (JSONObject) resultJsonArray2.getJSONObject(0);
    			JSONObject durationJsonObject = (JSONObject) resultJsonObject3.getJSONObject("duration");
    			String time = durationJsonObject.getString("text");
    			
    			return time;
    }
    
    private int getDriverIndexById(String id) { 
    	for (int i = 0; i < drivers.size(); i++)
    		if (drivers.get(i).getId().equals(id))
    			return i;
    	return -1;
    }
    
    private double distance(double lat1, double lon1, double lat2, double lon2) {
        double theta = lon1 - lon2;
        double dist = Math.sin(deg2rad(lat1)) * Math.sin(deg2rad(lat2)) + Math.cos(deg2rad(lat1)) * Math.cos(deg2rad(lat2)) * Math.cos(deg2rad(theta));
        dist = Math.acos(dist);
        dist = rad2deg(dist);
        dist = dist * 60 * 1.1515;

        return (dist);
      }
    
    private double deg2rad(double deg) {
    	return (deg * Math.PI / 180.0);
    }

    private double rad2deg(double rad) {
    	return (rad * 180.0 / Math.PI);
    }
}