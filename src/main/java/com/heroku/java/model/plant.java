package com.heroku.java.model;

import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.Inheritance;
import javax.persistence.InheritanceType;
import javax.persistence.Table;

@Entity
@Table(name = "plant")
@Inheritance(strategy = InheritanceType.JOINED)
public class plant {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer plantId;

    private String sciName;
    private String comName;
    private String type;
    private String habitat;
    private String species;
    private String description;

    private IndoorPlant IndoorPlant;
    private OutdoorPlant OutdoorPlant;

    // Default constructor
    public plant() {}

    // Getters and setters
    public Integer getPlantId() {
        return plantId;
    }

    public void setPlantId(Integer plantId) {
        this.plantId = plantId;
    }

    public String getSciName() {
        return sciName;
    }

    public void setSciName(String sciName) {
        this.sciName = sciName;
    }

    public String getComName() {
        return comName;
    }

    public void setComName(String comName) {
        this.comName = comName;
    }

    public String getType() {
        return type;
    }

    public void setType(String type) {
        this.type = type;
    }

    public String getHabitat() {
        return habitat;
    }

    public void setHabitat(String habitat) {
        this.habitat = habitat;
    }

    public String getSpecies() {
        return species;
    }

    public void setSpecies(String species) {
        this.species = species;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public IndoorPlant getIndoorPlant() {
        return IndoorPlant;
    }

    public void setIndoorPlant(IndoorPlant IndoorPlant) {
        this.IndoorPlant = IndoorPlant;
    }

    public OutdoorPlant getOutdoorPlant() {
        return OutdoorPlant;
    }

    public void setOutdoorPlant(OutdoorPlant OutdoorPlant) {
        this.OutdoorPlant = OutdoorPlant;
    }
}