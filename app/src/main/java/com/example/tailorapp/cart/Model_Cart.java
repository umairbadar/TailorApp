package com.example.tailorapp.cart;

public class Model_Cart {

    private String id;
    private String name;
    private String price;
    private byte[] image;
    private String fabric_details;
    private String measurements;
    private String pickupDate;
    private String pickupTime;
    private int amount;

    public Model_Cart(String id, String name, String price, byte[] image, String fabric_details, String measurements, String pickupDate, String pickupTime, int amount) {
        this.id = id;
        this.name = name;
        this.price = price;
        this.image = image;
        this.fabric_details = fabric_details;
        this.measurements = measurements;
        this.pickupDate = pickupDate;
        this.pickupTime = pickupTime;
        this.amount = amount;
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

    public byte[] getImage() {
        return image;
    }

    public String getPrice() {
        return price;
    }

    public int getAmount() {
        return amount;
    }
}
