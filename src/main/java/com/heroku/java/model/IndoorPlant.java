package com.heroku.java.model;

import javax.persistence.Entity;
import javax.persistence.PrimaryKeyJoinColumn;
import javax.persistence.Table;

@Entity
@Table(name = "indoor_plant")
@PrimaryKeyJoinColumn(name = "plantId")
public class IndoorPlant extends plant {

    private String lightr;
    private String humidp;
    private String waterf;

    // Default constructor
    public IndoorPlant() {
        super();
    }

    // Getters and setters
    public String getLightR() {
        return lightr;
    }

    public void setLightR(String lightr) {
        this.lightr = lightr;
    }

    public String getHumidP() {
        return humidp;
    }

    public void setHumidP(String humidp) {
        this.humidp = humidp;
    }

    public String getWaterF() {
        return waterf;
    }

    public void setWaterF(String waterf) {
        this.waterf = waterf;
    }
}