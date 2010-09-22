package com.gavin.hotukdeals;

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.util.ArrayList;
import java.util.List;

import org.apache.http.HttpEntity;
import org.apache.http.HttpResponse;
import org.apache.http.StatusLine;
import org.apache.http.client.HttpClient;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.impl.client.DefaultHttpClient;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import android.content.Context;
import android.content.pm.PackageInfo;
import android.content.pm.PackageManager;
import android.content.pm.PackageManager.NameNotFoundException;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.util.Log;

public class DataHelper {
	
	private static String sUserAgent = null;
    private static byte[] sBuffer = new byte[512];
    
    private Context context = null;
    
    public DataHelper(Context c)
    {
    	context = c;
    	DataHelper.prepareUserAgent(context);
    }
    
	public static void prepareUserAgent(Context context) {
        try {
            // Read package name and version number from manifest
            PackageManager manager = context.getPackageManager();
            PackageInfo info = manager.getPackageInfo(context.getPackageName(), 0);
            sUserAgent = String.format(context.getString(R.string.ua_template), info.packageName, info.versionName);

        } catch(NameNotFoundException e) {
            Log.e("DataHelper", "Couldn't find package information in PackageManager", e);
        }
    }
		
	public List<Deal> getResults(String apiKey, String forum, String category, boolean onlineOnly, String order, int page, boolean activeOnly, String search)
	{
		List<Deal> items = new ArrayList<Deal>();		
		String uri = String.format("http://api.hotukdeals.com/rest_api/v2/?key=%s&output=json&forum=%s&category=%s&online_offline=%s&order=%s&page=%s&exclude_expired=%s&search=%s", 
				apiKey,
				forum,
				category,
				onlineOnly ? "online" : "",
				order,
				page,
				activeOnly ? "true" : "",
				search);
		
		try {
			String content = getUrlContent(context, uri);
			JSONObject response = new JSONObject(content);
			JSONArray deals = response.getJSONObject("deals").getJSONArray("items");
			for(int i = 0; i < deals.length(); i++)
				items.add(new Deal((JSONObject)deals.get(i)));
		} catch (ApiException e) {
			e.printStackTrace();
		} catch (JSONException e) {
			e.printStackTrace();
		}		
		return items;
	}
	
	@SuppressWarnings("serial")
	public static class ApiException extends Exception {
		public ApiException(String detailMessage, Throwable throwable) {
            super(detailMessage, throwable);
        }

        public ApiException(String detailMessage) {
            super(detailMessage);
        }
    }
	
    @SuppressWarnings("serial")
	public static class ParseException extends Exception {

		public ParseException(String detailMessage, Throwable throwable) {
            super(detailMessage, throwable);
        }
    }
    
    protected static synchronized String getUrlContent(Context context, String url) throws ApiException {
    	if (sUserAgent == null)
    		throw new ApiException("User-Agent string must be prepared");

		// Create client and set our specific user-agent string
		HttpClient client = new DefaultHttpClient();
		HttpGet request = new HttpGet(url);
		request.setHeader("User-Agent", sUserAgent);
		
		try {
		    HttpResponse response = client.execute(request);
		    
		    StatusLine status = response.getStatusLine();
		    if (status.getStatusCode() != 200) {
		        throw new ApiException("Invalid response from server: " +
		                status.toString());
		    }		

		    HttpEntity entity = response.getEntity();
		    InputStream inputStream = entity.getContent();		
		    ByteArrayOutputStream content = new ByteArrayOutputStream();

		    int readBytes = 0;
		    sBuffer = new byte[512];
		    while ((readBytes = inputStream.read(sBuffer)) != -1) {
		        content.write(sBuffer, 0, readBytes);
		    }		

		    return new String(content.toByteArray());
		} catch (IOException e) {
		    throw new ApiException("Problem communicating with API", e);
		}
	}
}
