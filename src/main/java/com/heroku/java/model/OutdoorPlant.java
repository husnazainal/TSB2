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
    public String getSunE() {
        return sune;
    }

    public void setSunE(String sune) {
        this.sune = sune;
    }

    public String getWindR() {
        return windr;
    }

    public void setWindR(String windr) {
        this.windr = windr;
    }

    public String getSoilT() {
        return soilt;
    }

    public void setSoilT(String soilt) {
        this.soilt = soilt;
    }
}