package com.heroku.java.model;

public class PetFood {
    private int inventoryID;
    private String inventoryName;
    private String inventoryCategory;
    private String inventoryBrand;
    private double inventoryPrice;
    private int inventoryQuantityExisting;
    private int inventoryReorderPoint;
    private int foodWeight;
    private int quantityIn;
    private String foodType;

    // Getters and Setters
    public int getInventoryID() {
        return inventoryID;
    }

    public void setInventoryID(int inventoryID) {
        this.inventoryID = inventoryID;
    }

    public String getInventoryName() {
        return inventoryName;
    }

    public void setInventoryName(String inventoryName) {
        this.inventoryName = inventoryName;
    }

    public String getInventoryCategory() {
        return inventoryCategory;
    }

    public void setInventoryCategory(String inventoryCategory) {
        this.inventoryCategory = inventoryCategory;
    }

    public String getInventoryBrand() {
        return inventoryBrand;
    }

    public void setInventoryBrand(String inventoryBrand) {
        this.inventoryBrand = inventoryBrand;
    }

    public double getInventoryPrice() {
        return inventoryPrice;
    }

    public void setInventoryPrice(double inventoryPrice) {
        this.inventoryPrice = inventoryPrice;
    }

    public int getInventoryQuantityExisting() {
        return inventoryQuantityExisting;
    }

    public void setInventoryQuantityExisting(int inventoryQuantityExisting) {
        this.inventoryQuantityExisting = inventoryQuantityExisting;
    }

    public int getInventoryReorderPoint() {
        return inventoryReorderPoint;
    }

    public void setInventoryReorderPoint(int inventoryReorderPoint) {
        this.inventoryReorderPoint = inventoryReorderPoint;
    }

    public int getFoodWeight() {
        return foodWeight;
    }

    public void setFoodWeight(int foodWeight) {
        this.foodWeight = foodWeight;
    }

    public int getQuantityIn() {
        return quantityIn;
    }

    public void setQuantityIn(int quantityIn) {
        this.quantityIn = quantityIn;
    }

    public String getFoodType() {
        return foodType;
    }

    public void setFoodType(String foodType) {
        this.foodType = foodType;
    }
}
