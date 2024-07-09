package com.heroku.java.model;

import java.sql.Date;
import java.util.Objects;

import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.Column;
import javax.persistence.Temporal;
import javax.persistence.TemporalType;

@Entity
public class Feedback {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private int feedbackId;

    @Column(nullable = false)
    private int plantId;

    @Column(nullable = false)
    private String message;

    @Temporal(TemporalType.TIMESTAMP)
    @Column(name = "dateCreated")
    private Date dateCreated;

    @Column(name = "visitorName")
    private String visitorName;

    @Column(name = "visitorId")
    private int visitorId;

    public Feedback(int feedbackId, int plantId, String message, Date dateCreated, String visitorName, int visitorId) {
        this.feedbackId = feedbackId;
        this.plantId = plantId;
        this.message = message;
        this.dateCreated = dateCreated;
        this.visitorName = visitorName;
        this.visitorId = visitorId;
    }

    public Feedback() {}

	public int getFeedbackId() {
    	return feedbackId;
    }
    
    public int getPlantId() {
    	return plantId;
    }
    
    public String getMessage() {
    	return message;
    }
    
    public Date getDateCreated() {
    	return dateCreated;
    }
    
    public String getVisitorName() {
    	return visitorName;
    }
    
    public int getVisitorId() {
    	return visitorId;
    }
    
    public void setFeedbackId(int feedbackId) {
    	this.feedbackId = feedbackId;
    }
    
    public void setPlantId(int plantId) {
    	this.plantId = plantId;
    }
    
    public void setMessage(String message) {
    	this.message = message;
    }
    
    public void setDateCreated(Date dateCreated) {
    	this.dateCreated = dateCreated;
    }
    
    public void setVisitorName(String visitorName) {
    	this.visitorName = visitorName;
    }
    
    public void setVisitorId(int visitorId) {
    	this.visitorId = visitorId;
    }

    @Override
    public String toString() {
        return "Feedback{" +
               "feedbackId=" + feedbackId +
               ", plantId=" + plantId +
               ", message='" + message + '\'' +
               ", dateCreated=" + dateCreated +
               ", visitorName='" + visitorName + '\'' +
               ", visitorId=" + visitorId +
               '}';
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        Feedback feedback = (Feedback) o;
        return feedbackId == feedback.feedbackId &&
               plantId == feedback.plantId &&
               visitorId == feedback.visitorId &&
               Objects.equals(message, feedback.message) &&
               Objects.equals(dateCreated, feedback.dateCreated) &&
               Objects.equals(visitorName, feedback.visitorName);
    }

    @Override
    public int hashCode() {
        return Objects.hash(feedbackId, plantId, message, dateCreated, visitorName, visitorId);
    }

}