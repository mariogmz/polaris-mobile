package com.evologics.polaris.controller;

public interface UserStore {
	
	void setUserEmail(String email);
	
	String getUserEmail();
	
	void setUserAuthToken(String authToken);
	
	String getUserAuthToken();
}
