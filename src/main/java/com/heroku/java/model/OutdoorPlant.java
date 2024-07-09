package com.heroku.java.model;

import javax.persistence.Entity;
import javax.persistence.PrimaryKeyJoinColumn;
import javax.persistence.Table;

@Entity
@Table(name = "outdoor_plant")
@PrimaryKeyJoinColumn(name = "plantId")
public class OutdoorPlant extends plant {

    private String sune;
    private String windr;
    private String soilt;

    // Default constructor
    public OutdoorPlant() {
        super();
    }

    // Getters and setters
    public String getSune() {
        return sune;
    }

    public void setSune(String sune) {
        this.sune = sune;
    }

    public String getWindr() {
        return windr;
    }

    public void setWindr(String windr) {
        this.windr = windr;
    }

    public String getSoilt() {
        return soilt;
    }

    public void setSoilt(String soilt) {
        this.soilt = soilt;
    }
}