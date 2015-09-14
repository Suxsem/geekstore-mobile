package com.suxsem.geekstore.business;

import java.io.IOException;
import java.net.URLEncoder;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import com.google.android.gms.maps.model.LatLng;

public class Geocoder {

	
	// query the google place api service to obtain the gps position of the store
	
	public LatLng getLatLongFromAddress(String place) {
		
		try {
			
			// set the google place api uri
			
			String uri = "https://maps.googleapis.com/maps/api/place/textsearch/json?query=" +
					URLEncoder.encode(place, "utf-8") + "&key=AIzaSyAGSg6BfqWCNc5Gwc0eFBg5V5uA0tyA4Ro";
			
			// create a new JsonDownloader object and execute the request
			
			JsonDownloader jsonDownloader = new JsonDownloader();
			JSONObject response = new JSONObject(jsonDownloader.getJson(uri));
			
			// parse the json response to get lat and lng of the store

			double lat = ((JSONArray)response.get("results")).getJSONObject(0)
					.getJSONObject("geometry").getJSONObject("location")
					.getDouble("lat");
			
			double lng = ((JSONArray)response.get("results")).getJSONObject(0)
					.getJSONObject("geometry").getJSONObject("location")
					.getDouble("lng");
			
			// return a new LatLng object from the gps position
			
			return new LatLng(lat, lng);
			
		} catch (NullPointerException e) {
			e.printStackTrace();
		} catch (IOException e) {
			e.printStackTrace();
		} catch (JSONException e) {
			e.printStackTrace();
		}
		
		// return a fake position as fallback
		
		return new LatLng(0, 0);

	}

}
