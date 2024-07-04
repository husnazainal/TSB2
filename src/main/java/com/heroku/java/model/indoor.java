package com.heroku.java.model;

public class indoor extends plant {
    private String lightR;
    private String humidP;
    private String waterF;

    public indoor() {
        super();
    }

    public indoor(int plantId, String plantSciname, String plantComname, String plantType, String lightRequirements,
            String humidityPreferences, String wateringFrequency, String sunlightExposure, String windResistance,
            String soilType, String plantHabitat, String plantSpecies, String plantDesc,
            String lightR, String humidP, String waterF) {
        super();
        this.lightR = lightR;
        this.humidP = humidP;
        this.waterF = waterF;
    }

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
