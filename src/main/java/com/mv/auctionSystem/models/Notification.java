package com.mv.auctionSystem.models;

import java.util.Date;

public class Notification {
	private String message;
	private Date dateTime;

	public Notification(String message, Date dateTime) {
		this.message = message;
		this.dateTime = dateTime;
	}

	public String getMessage() {
		return message;
	}

	public void setMessage(String message) {
		this.message = message;
	}

	public Date getDateTime() {
		return dateTime;
	}

	public void setDateTime(Date dateTime) {
		this.dateTime = dateTime;
	}

	@Override
	public String toString() {
		return "Date: " + this.dateTime.toString() + ", " + "Message: " + this.message;
	}
}
