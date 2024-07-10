package com.heroku.java.model;

import java.util.Objects;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;

@Entity
public class Visitor {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private int visitorId;

    @Column(nullable = false)
    private String visitorName;

    @Column(nullable = false)
    private String visitorEmail;

    @Column(name = "visitorPhoneNum")
    private String visitorPhoneNum;

    public Visitor(int visitorId, String visitorName, String visitorEmail, String visitorPhoneNum) {
        this.visitorId = visitorId;
        this.visitorName = visitorName;
        this.visitorEmail = visitorEmail;
        this.visitorPhoneNum = visitorPhoneNum;
    }

    public Visitor() {}

    public int getVisitorId() {
    	return visitorId;
    }
    
    public String getVisitorName() {
    	return visitorName;
    }
    
    public String getVisitorEmail() {
    	return visitorEmail;
    }
    
    public String getVisitorPhoneNum() {
    	return visitorPhoneNum;
    }
    
    public void setVisitorId(int visitorId) {
    	this.visitorId = visitorId;
    }
    
    public void setVisitorName(String visitorName) {
    	this.visitorName = visitorName;
    }
    
    public void setVisitorEmail(String visitorEmail) {
    	this.visitorEmail = visitorEmail;
    }
    
    public void setVisitorPhoneNum(String visitorPhoneNum) {
    	this.visitorPhoneNum = visitorPhoneNum;
    }

    @Override
    public String toString() {
        return "Visitor{" +
               "visitorId=" + visitorId +
               ", visitorName='" + visitorName + '\'' +
               ", visitorEmail='" + visitorEmail + '\'' +
               ", visitorPhoneNum='" + visitorPhoneNum + '\'' +
               '}';
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        Visitor visitor = (Visitor) o;
        return visitorId == visitor.visitorId &&
               Objects.equals(visitorName, visitor.visitorName) &&
               Objects.equals(visitorEmail, visitor.visitorEmail) &&
               Objects.equals(visitorPhoneNum, visitor.visitorPhoneNum);
    }

    @Override
    public int hashCode() {
        return Objects.hash(visitorId, visitorName, visitorEmail, visitorPhoneNum);
    }
}