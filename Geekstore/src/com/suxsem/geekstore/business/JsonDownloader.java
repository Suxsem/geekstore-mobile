package com.suxsem.geekstore.business;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;
import android.util.Base64;

public class JsonDownloader {

	// methods overloading to support both unauthenticated and authenticated json requests
	
	public String getJson(String url) {
		return process(url, null, null);
	}

	public String getJson(String url, String username, String password) {
		return process(url, username, password);
	}
	
	// execute a json query and return the response
	
	private String process(String url, String username, String password) {
	    HttpURLConnection c = null;
	    try {
	    	
	    	// set some request headers
	    	
	    	final int timeout = 25000;
	        URL u = new URL(url);
	        c = (HttpURLConnection) u.openConnection();
	        c.setRequestMethod("GET");
	        c.setRequestProperty("Content-length", "0");
	        c.setUseCaches(false);
	        c.setAllowUserInteraction(false);
	        c.setConnectTimeout(timeout);
	        c.setReadTimeout(timeout);
	        
	        // if present, add basic auth credentials to the request header
	        
	        if (username != null && password != null) {
	        	String encoded = Base64.encodeToString((username + ":" + password).getBytes("UTF-8"), Base64.NO_WRAP);
	        	c.setRequestProperty("Authorization", "Basic " + encoded);
	        }

	        // execute the request and get the response code
	        
	        c.connect();
	        int status = c.getResponseCode();

	        switch (status) {
	            case 200:
	            case 201:
	            	
	            	// if the request is valid download the response payload
	            	
	                BufferedReader br = new BufferedReader(new InputStreamReader(c.getInputStream()));
	                StringBuilder sb = new StringBuilder();
	                String line;
	                while ((line = br.readLine()) != null) {
	                    sb.append(line+"\n");
	                }
	                br.close();
	                return sb.toString();
	        }

	    } catch (MalformedURLException ex) {
	        ex.printStackTrace();
	    } catch (IOException ex) {
	    	ex.printStackTrace();
		} finally {
	       if (c != null) {
	          try {
	              c.disconnect();
	          } catch (Exception ex) {
	        	  ex.printStackTrace();
	          }
	       }
	    }
	    return null;
	}
	
}