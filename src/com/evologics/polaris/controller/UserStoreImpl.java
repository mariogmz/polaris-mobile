package com.evologics.polaris.controller;

import com.evologics.polaris.model.User;

public class UserStoreImpl implements UserStore{
	private static UserStoreImpl userStore = null;
	
	private User user;
	
	private UserStoreImpl() {
		this.user = new User();
	}
	
	public static UserStoreImpl getInstance() {
		if( userStore == null ){
			userStore = new UserStoreImpl(); 
		}
		return userStore;
	}
	
	@Override
	public void setUserEmail(String email){
		this.user.setEmail(email);
	}
	
	@Override
	public String getUserEmail(){
		return this.user.getEmail();
	}
	
	@Override
	public void setUserAuthToken(String authToken){
		this.user.setAuthToken(authToken);
	}
	
	@Override
	public String getUserAuthToken(){
		return this.user.getAuthToken();
	}
	
}
