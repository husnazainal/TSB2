package com.heroku.java.model;

import java.sql.Date;
import java.util.Objects;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.FetchType;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.Table;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.Pattern;

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

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "plantid", insertable = false, updatable = false)
    private plant plant;

    @NotBlank(message = "Visitor name is required")
    @Column(name = "visitorname", nullable = false)
    private String visitorName;

    @NotBlank(message = "Email is required")
    @Email(message = "Please provide a valid email address")
    @Column(name = "visitoremail", nullable = false)
    private String visitorEmail;

    @NotBlank(message = "Phone number is required")
    @Pattern(regexp = "^01\\d \\d{7,8}$", message = "Phone number must be in the format 012 3456789 or 012 34567899")
    @Column(name = "visitorphoneno", nullable = false)
    private String visitorPhoneno;

    public feedback(int feedbackId, int plantId, String message, Date dateCreated, String visitorName, String visitorEmail, String visitorPhoneno) {
        this.feedbackId = feedbackId;
        this.plantId = plantId;
        this.message = message;
        this.dateCreated = dateCreated;
        this.visitorName = visitorName;
        this.visitorEmail = visitorEmail;
        this.visitorPhoneno = visitorPhoneno;
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
                + ", visitorEmail='" + visitorEmail + '\''
                + ", visitorPhoneno='" + visitorPhoneno + '\''
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
                && Objects.equals(visitorName, feedback.visitorName)
                && Objects.equals(visitorEmail, feedback.visitorEmail)
                && Objects.equals(visitorPhoneno, feedback.visitorPhoneno);
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

    public String getVisitorEmail() {
        return visitorEmail;
    }

    public void setVisitorEmail(String visitorEmail) {
        this.visitorEmail = visitorEmail;
    }

    public String getVisitorPhoneno() {
        return visitorPhoneno;
    }

    public void setVisitorPhoneno(String visitorPhoneno) {
        this.visitorPhoneno = visitorPhoneno;
    }

}
