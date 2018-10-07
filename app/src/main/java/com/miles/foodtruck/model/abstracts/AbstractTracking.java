package com.miles.foodtruck.model.abstracts;

import com.miles.foodtruck.model.interfaces.TrackingInterface;

import java.util.Date;

public abstract class AbstractTracking implements TrackingInterface{
    private String trackingId;
    private int trackableId;
    private String title;
    private Date targetStartTime;
    private Date targetEndTime;
    private Date meetTime;
    private String currLocation;
    private String meetLocation;
    private int travelTime;

    public AbstractTracking(){
    }

    public String getTrackingId() {
        return trackingId;
    }

    public void setTrackingId(String trackingId) {
        this.trackingId = trackingId;
    }

    public int getTrackableId() {
        return trackableId;
    }

    public void setTrackableId(int trackableId) {
        this.trackableId = trackableId;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public Date getTargetStartTime() {
        return targetStartTime;
    }

    public void setTargetStartTime(Date targetStartTime) {
        this.targetStartTime = targetStartTime;
    }

    public Date getTargetEndTime() {
        return targetEndTime;
    }

    public void setTargetEndTime(Date targetEndTime) {
        this.targetEndTime = targetEndTime;
    }

    public Date getMeetTime() {
        return meetTime;
    }

    public void setMeetTime(Date meetTime) {
        this.meetTime = meetTime;
    }

    public String getCurrLocation() {
        return currLocation;
    }

    public void setCurrLocation(String currLocation) {
        this.currLocation = currLocation;
    }

    public String getMeetLocation() {
        return meetLocation;
    }

    public void setMeetLocation(String meetLocation) {
        this.meetLocation = meetLocation;
    }

    public int getTravelTime() {
        return travelTime;
    }

    public void setTravelTime(int travelTime) {
        this.travelTime = travelTime;
    }

    @Override
    public String getOutPutString() {

        return "Title: " + getTitle() + "\n"
                + "Meet Time: " + getMeetTime() + "\n"
                + "Meet Location: " + getMeetLocation() + "\n"
                + "Current Location: " + getCurrLocation() + "\n"
                + "Travel Time: " + getTravelTime()/60 + "mins";
    }
}
