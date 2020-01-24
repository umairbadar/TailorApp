package com.example.tailorapp.tabLayout.category;

public class Model_Category {

    private String id;
    private String name;
    private String price;
    private String image;

    public Model_Category(String id, String name, String price, String image) {
        this.id = id;
        this.name = name;
        this.price = price;
        this.image = image;
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
