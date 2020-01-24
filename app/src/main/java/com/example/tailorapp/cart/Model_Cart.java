package com.example.tailorapp.cart;

public class Model_Cart {

    private String id;
    private String name;
    private String price;
    private String image;
    private String fabric_details;
    private String measurements;
    private String pickupDate;
    private String pickupTime;


    public Model_Cart(String id, String name, String price, String image, String fabric_details, String measurements, String pickupDate, String pickupTime) {
        this.id = id;
        this.name = name;
        this.price = price;
        this.image = image;
        this.fabric_details = fabric_details;
        this.measurements = measurements;
        this.pickupDate = pickupDate;
        this.pickupTime = pickupTime;
    }

    public String getFabric_details() {
        return fabric_details;
    }

    public String getMeasurements() {
        return measurements;
    }

    public String getPickupDate() {
        return pickupDate;
    }

    public String getPickupTime() {
        return pickupTime;
    }

    public String getId() {
        return id;
    }

    public String getName() {
        return name;
    }

    public String getPrice() {
        return price;
    }

    public String getImage() {
        return image;
    }
}
