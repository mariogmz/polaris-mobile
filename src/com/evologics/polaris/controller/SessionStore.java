package com.evologics.polaris.controller;

import org.apache.http.client.HttpClient;
import org.apache.http.impl.client.DefaultHttpClient;
import org.apache.http.params.BasicHttpParams;
import org.apache.http.params.HttpConnectionParams;
import org.apache.http.params.HttpParams;

public class SessionStore {
	public static SessionStore instance = null;
	
	private HttpClient client;
	
	private SessionStore(){
		init();
	};
	
	private void init(){
		int TIMEOUT_MILLISEC = 10000;  // = 10 seconds
		HttpParams httpParams = new BasicHttpParams();
		HttpConnectionParams.setConnectionTimeout(httpParams, TIMEOUT_MILLISEC);
		HttpConnectionParams.setSoTimeout(httpParams, TIMEOUT_MILLISEC);
		client = new DefaultHttpClient(httpParams);
	}
	
	public static SessionStore getInstance(){
		if( instance == null ){
			instance = new SessionStore();
		}
		return instance;
	}

	public static void setInstance(SessionStore instance) {
		SessionStore.instance = instance;
	}

	public HttpClient getClient() {
		return client;
	}

	public void setClient(HttpClient client) {
		this.client = client;
	}
	
	
	
	

}
