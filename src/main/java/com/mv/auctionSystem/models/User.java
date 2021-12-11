package com.mv.auctionSystem.models;

import com.sun.istack.internal.NotNull;

import java.util.LinkedList;
import java.util.List;

public class User extends Observer {
	private String userId;
	private String username;
	private String emailId;
	private String phoneNo;
	private List<Notification> notifications; //Always insert latest data at the head of the list.
	// TODO - private List<BankAccountDetails> bankAccountDetils;
	// TODO - private List<Address> addresses;

	public User(@NotNull String userId, @NotNull String username, @NotNull String emailId, @NotNull String phoneNo) {
		this.userId = userId;
		this.username = username;
		this.emailId = emailId;
		this.phoneNo = phoneNo;
		this.notifications = new LinkedList<>();
	}

	public String getUserId() {
		return userId;
	}

	public void setUserId(String userId) {
		this.userId = userId;
	}
	
	public String getUsername() {
		return username;
	}

	public void setUsername(String username) {
		this.username = username;
	}

	public String getEmailId() {
		return emailId;
	}

	public void setEmailId(String emailId) {
		this.emailId = emailId;
	}

	public String getPhoneNo() {
		return phoneNo;
	}

	public void setPhoneNo(String phoneNo) {
		this.phoneNo = phoneNo;
	}

	public List<Notification> getNotifications() {
		return notifications;
	}

	@Override
	public boolean equals(Object obj) {
		if(obj==null) {
			return false;
		}
		if(obj instanceof User) {
			return this.username.equals(((User) obj).getUsername());
		}
		return false;
	}
}