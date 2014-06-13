package com.evologics.polaris.controller;

import com.evologics.polaris.model.User;

public interface UserStore {
	
	void setUserEmail(String email);
	
	String getUserEmail();
	
	void setUserAuthToken(String authToken);
	
	String getUserAuthToken();
}
