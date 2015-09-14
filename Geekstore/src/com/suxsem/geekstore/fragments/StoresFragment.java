package com.suxsem.geekstore.fragments;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import com.google.android.gms.maps.CameraUpdate;
import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.suxsem.geekstore.R;
import com.suxsem.geekstore.business.Geocoder;
import com.suxsem.geekstore.business.JsonDownloader;

import android.support.v4.app.Fragment;
import android.app.ProgressDialog;
import android.os.AsyncTask;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.google.android.gms.maps.SupportMapFragment;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.LatLngBounds;
import com.google.android.gms.maps.model.MarkerOptions;

public class StoresFragment extends Fragment {

	private Fragment fragment = this;
	private GoogleMap map;

	// returns a new instance of this fragment

	public static StoresFragment newInstance() {
		StoresFragment fragment = new StoresFragment();
		return fragment;
	}

	public StoresFragment() {
	}

	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
		View rootView = inflater.inflate(R.layout.fragment_stores, container, false);
		
		// get the google map view reference
		
		map = ((SupportMapFragment) getChildFragmentManager().findFragmentById(R.id.map)).getMap();
		
		// display map toolbar to allow users to open a store in Google Maps app or start directions
		
		map.getUiSettings().setMapToolbarEnabled(true);
		
		// start the JsonParser AsyncTask passing

		new JsonParser().execute();
		
		return rootView;
	}		

	private class JsonParser extends AsyncTask<Void, Void, List<MarkerOptions>> {
		private ProgressDialog pDialog;

		@Override
		protected void onPreExecute() {
			super.onPreExecute();
			
			// show a ProgressDialog to block the user interaction
			
			pDialog = new ProgressDialog(fragment.getActivity());
			pDialog.setMessage("Recupero i punti vendita...");
			pDialog.setIndeterminate(true);
			pDialog.setCancelable(false);
			pDialog.show();
		}

		@Override
		protected List<MarkerOptions> doInBackground(Void... args) {
			
			// create a new JsonDownloader object
			
			JsonDownloader jsonDownloader = new JsonDownloader();
			
			// create an empty markers list as fallback if something goes wrong

			List<MarkerOptions> markers = new ArrayList<MarkerOptions>();

			try {
				
				// query the main server for stores

				JSONArray stores = new JSONArray(jsonDownloader.getJson("https://geekstore-suxsem.herokuapp.com/stores.json"));
				
				// create a new Geocoder object to geolocate the stores
				
				Geocoder gc = new Geocoder();
				
				// cycle through all order stores						
				
				for (int i = 0; i < stores.length(); i++) {
					
					// parse a store json object
					
					JSONObject store = stores.getJSONObject(i);
					String place = store.getString("place");
					
					// ask the Geocoder to locate the store
					
					LatLng latLng = gc.getLatLongFromAddress(place);
					
					// create a new marker from the latLng object
					
					markers.add(new MarkerOptions().position(latLng).title(place));
					
				}
			} catch (NullPointerException e) {
				e.printStackTrace();
			} catch (JSONException e) {
				e.printStackTrace();
			}

			// returns the markers list
			
			return markers;
			
		}
		
		@Override
		protected void onPostExecute(List<MarkerOptions> markers) {
			
			// create a new Iterator object to cycle through the markers
			
			Iterator<MarkerOptions> itr = markers.iterator();
			
			// compute the smallest area that can contain all markers at the same time
			
			LatLngBounds.Builder builder = new LatLngBounds.Builder();			
			while(itr.hasNext()) {
				MarkerOptions marker = itr.next();
				map.addMarker(marker);
				builder.include(marker.getPosition());
			}
			LatLngBounds bounds = builder.build();
			
			// move the map camera to the computed area
			
			CameraUpdate cu = CameraUpdateFactory.newLatLngBounds(bounds, 100);
			map.animateCamera(cu);

			// dismiss the waiting dialog
			
			pDialog.dismiss();
		}
	}
}