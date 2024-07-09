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
    public String getLightr() {
        return lightr;
    }

    public void setLightr(String lightr) {
        this.lightr = lightr;
    }

    public String getHumidp() {
        return humidp;
    }

    public void setHumidp(String humidp) {
        this.humidp = humidp;
    }

    public String getWaterf() {
        return waterf;
    }

    public void setWaterf(String waterf) {
        this.waterf = waterf;
    }
}