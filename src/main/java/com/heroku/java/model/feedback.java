package com.heroku.java.model;

import java.sql.Date;
import java.util.Objects;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;


import jakarta.persistence.*;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;

@Entity
@Table(name = "feedback")
public class feedback {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "feedbackid")
    private int feedbackId;

    @Column(name = "plantid", nullable = false)
    private int plantId;

    @Column(nullable = false)
    private String message;

    @Column(name = "datecreated")
    private Date dateCreated;

    @Column(name = "visitorname", nullable = false)
    private String visitorName;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "plantid", insertable = false, updatable = false)
    private plant plant;

    public feedback(int feedbackId, int plantId, String message, Date dateCreated, String visitorName) {
        this.feedbackId = feedbackId;
        this.plantId = plantId;
        this.message = message;
        this.dateCreated = dateCreated;
        this.visitorName = visitorName;
    }

    public feedback() {
    }

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

    @Override
    public String toString() {
        return "Feedback{"
                + "feedbackId=" + feedbackId
                + ", plantId=" + plantId
                + ", message='" + message + '\''
                + ", dateCreated=" + dateCreated
                + ", visitorName='" + visitorName + '\''
                + '}';
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (o == null || getClass() != o.getClass()) {
            return false;
        }
        feedback feedback = (feedback) o;
        return feedbackId == feedback.feedbackId
                && plantId == feedback.plantId
                && Objects.equals(message, feedback.message)
                && Objects.equals(dateCreated, feedback.dateCreated)
                && Objects.equals(visitorName, feedback.visitorName);
    }

    @Override
    public int hashCode() {
        return Objects.hash(feedbackId, plantId, message, dateCreated, visitorName);
    }

    public plant getPlant() {
        return plant;
    }

    public void setPlant(plant plant) {
        this.plant = plant;
    }

}
