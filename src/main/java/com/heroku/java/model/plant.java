package com.heroku.java.model;

import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.Inheritance;
import jakarta.persistence.InheritanceType;
import jakarta.persistence.Table;
import jakarta.persistence.OneToOne;
import jakarta.persistence.CascadeType;

@Entity
@Table(name = "plant")
@Inheritance(strategy = InheritanceType.JOINED)
public class plant {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private int plantId;

    private String sciName;
    private String comName;
    private String type;
    private String habitat;
    private String species;
    private String description;

    @OneToOne(mappedBy = "plant", cascade = CascadeType.ALL)
    private IndoorPlant IndoorPlant;

    @OneToOne(mappedBy = "plant", cascade = CascadeType.ALL)
    private OutdoorPlant OutdoorPlant;

    // Default constructor
    public plant() {
    }

    // Getters and setters
    public int getPlantId() {
        return plantId;
    }

    public void setPlantId(int plantId) {
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
