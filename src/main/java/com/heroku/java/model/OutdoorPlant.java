package com.heroku.java.model;

import jakarta.persistence.Entity;
import jakarta.persistence.PrimaryKeyJoinColumn;
import jakarta.persistence.Table;

@Entity
@Table(name = "outdoor_plant")
@PrimaryKeyJoinColumn(name = "plantId")
public class OutdoorPlant extends plant {

    private String sunE;
    private String windR;
    private String soilT;

    // Default constructor
    public OutdoorPlant() {
        super();
    }

    // Getters and setters
    public String getSunE() {
        return sunE;
    }

    public void setSunE(String sunE) {
        this.sunE = sunE;
    }

    public String getWindR() {
        return windR;
    }

    public void setWindR(String windR) {
        this.windR = windR;
    }

    public String getSoilT() {
        return soilT;
    }

    public void setSoilT(String soilT) {
        this.soilT = soilT;
    }
}