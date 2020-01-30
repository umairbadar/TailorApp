package com.example.tailorapp.tabLayout.category;

public class Model_Category {

    private String id;
    private String name;
    private String price;
    private String image;
    private int amount;

    public Model_Category(String id, String name, String price, String image, int amount) {
        this.id = id;
        this.name = name;
        this.price = price;
        this.image = image;
        this.amount = amount;
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

    public int getAmount() {
        return amount;
    }
}
