package vozie.ridesharing.app;

import org.json.*;
import java.net.HttpURLConnection;
import java.net.URL;
import java.io.InputStreamReader;
import java.io.InputStream;
import java.io.DataOutputStream;
import java.io.BufferedReader;


public class LocationService {
	private final String command;
	private final String arg1, arg2, arg3, arg4;
	private final String apiKey = "AIzaSyCzFInTxNdp-3PVnjkGOASWm3lT96QfrDY";
	
    public LocationService(String command, String arg1, String arg2, String arg3, String arg4) {
    	this.command = command;
    	this.arg1 = arg1;
    	this.arg2 = arg2;
    	this.arg3 = arg3;
    	this.arg4 = arg4;
    }
    
    public String getContent() {
    	switch(command) {
    		case "getEstimatedTripTime":
    			String queryUrl = "https://maps.googleapis.com/maps/api/directions/json?origin=" + arg1 + 
    			"," + arg2 + "&destination=" + arg3 + "," + arg4 + "&key=" + apiKey;
    			JSONObject resultJson = new JSONObject(executePost(queryUrl));
    			JSONArray resultJsonArray = (JSONArray) resultJson.get("routes");
    			JSONObject resultJsonObject1 = (JSONObject) resultJsonArray.getJSONObject(0);
    			JSONArray resultJsonArray2 = (JSONArray) resultJsonObject1.getJSONArray("legs");
    			JSONObject resultJsonObject3 = (JSONObject) resultJsonArray2.getJSONObject(0);
    			JSONObject durationJsonObject = (JSONObject) resultJsonObject3.getJSONObject("duration");
    			String time = durationJsonObject.getString("text");
    			
    			return time;
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

}