package com.mv.auctionSystem.models;

import com.mv.auctionSystem.enums.PublisherEventType;

import java.util.Date;

public class Event {
    private PublisherEventType eventType;
    private String message;
    private Date dateTime;

    public PublisherEventType getEventType() {
        return eventType;
    }

    public void setEventType(PublisherEventType eventType) {
        this.eventType = eventType;
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
}
