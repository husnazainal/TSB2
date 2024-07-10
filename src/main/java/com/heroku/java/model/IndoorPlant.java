package com.heroku.java.model;

import jakarta.persistence.Entity;
import jakarta.persistence.PrimaryKeyJoinColumn;
import jakarta.persistence.Table;

@Entity
@Table(name = "indoor_plant")
@PrimaryKeyJoinColumn(name = "plantId")
public class IndoorPlant extends plant {

    private String lightR;
    private String humidP;
    private String waterF;

    // Default constructor
    public IndoorPlant() {
        super();
    }

    // Getters and setters
    public String getLightR() {
        return lightR;
    }

    public void setLightR(String lightR) {
        this.lightR = lightR;
    }

    public String getHumidP() {
        return humidP;
    }

    public void setHumidP(String humidP) {
        this.humidP = humidP;
    }

    public String getWaterF() {
        return waterF;
    }

    public void setWaterF(String waterF) {
        this.waterF = waterF;
    }
}