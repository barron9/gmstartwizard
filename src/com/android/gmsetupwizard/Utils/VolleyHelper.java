package com.android.gmsetupwizard.Utils;

import android.content.Context;
import android.util.ArrayMap;
import android.util.Base64;

import java.io.UnsupportedEncodingException;
import java.util.Map;

import org.json.JSONObject;

import com.android.volley.AuthFailureError;
import com.android.volley.Request.Method;
import com.android.volley.RequestQueue;
import com.android.volley.Response.ErrorListener;
import com.android.volley.Response.Listener;
import com.android.volley.toolbox.ImageLoader;
import com.android.volley.toolbox.JsonObjectRequest;
import com.android.volley.toolbox.Volley;

public class VolleyHelper {

	private final Context mContext;
	private final RequestQueue mRequestQueue;
	private final String mBaseUrl;

	public VolleyHelper(Context c, String baseUrl) {
		mContext = c;
		mRequestQueue = Volley.newRequestQueue(mContext);
		mBaseUrl = baseUrl;
	}

	private String contructUrl(String method) {
		return mBaseUrl + "/" + method;
	}

	public void get(String method, JSONObject jsonRequest, Listener<JSONObject> listener, ErrorListener errorListener) {

		JsonObjectRequest objRequest = new JsonObjectRequest(Method.GET, contructUrl(method), jsonRequest, listener,
				errorListener) {
			@Override
			public Map<String, String> getHeaders() throws AuthFailureError {
				Map<String, String> map = new ArrayMap();
				map.put("apikey", "a102fa1a9489c47f28f4a2705d0f1e1f1");
				return map;
			}
		};
		mRequestQueue.add(objRequest);
	}

	public void put(String method, JSONObject jsonRequest, Listener<JSONObject> listener, ErrorListener errorListener) {

		JsonObjectRequest objRequest = new JsonObjectRequest(Method.PUT, contructUrl(method), jsonRequest, listener,
				errorListener) {
			@Override
			public Map<String, String> getHeaders() throws AuthFailureError {
				Map<String, String> map = new ArrayMap();
				map.put("apikey", "a102fa1a9489c47f28f4a2705d0f1e1f1");
				return map;
			}
		};
		mRequestQueue.add(objRequest);
	}

	public void put(String method, JSONObject jsonRequest, Listener<JSONObject> listener, ErrorListener errorListener,
			String msisdn, String password) {
		byte[] data = null;
		try {
			data = (msisdn + ":" + password).getBytes("UTF-8");
		} catch (UnsupportedEncodingException e1) {
			e1.printStackTrace();
		}
		final byte[] finalData = data;
		JsonObjectRequest objRequest = new JsonObjectRequest(Method.PUT, contructUrl(method), jsonRequest, listener,
				errorListener) {
			@Override
			public Map<String, String> getHeaders() throws AuthFailureError {
				Map<String, String> map = new ArrayMap();
				map.put("Authorization", "Basic " + Base64.encodeToString(finalData, Base64.DEFAULT));
				return map;
			}
		};
		mRequestQueue.add(objRequest);
	}

	public void post(String method, JSONObject jsonRequest, Listener<JSONObject> listener,
			ErrorListener errorListener) {

		JsonObjectRequest objRequest = new JsonObjectRequest(Method.POST, contructUrl(method), jsonRequest, listener,
				errorListener) {
			@Override
			public Map<String, String> getHeaders() throws AuthFailureError {
				Map<String, String> map = new ArrayMap();
				map.put("apikey", "a102fa1a9489c47f28f4a2705d0f1e1f1");
				return map;
			}
		};

		mRequestQueue.add(objRequest);
	}

	public void delete(String method, JSONObject jsonRequest, Listener<JSONObject> listener,
			ErrorListener errorListener) {

		JsonObjectRequest objRequest = new JsonObjectRequest(Method.DELETE, contructUrl(method), jsonRequest, listener,
				errorListener) {
			@Override
			public Map<String, String> getHeaders() throws AuthFailureError {
				Map<String, String> map = new ArrayMap();
				map.put("apikey", "a102fa1a9489c47f28f4a2705d0f1e1f1");
				return map;
			}
		};
		mRequestQueue.add(objRequest);
	}

}
