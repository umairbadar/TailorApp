package com.example.tailorapp.order;

public class Model_OrderHistory {

    private String order_id;
    private String name;
    private String status;
    private String date;
    private String products;
    private String total;

    public Model_OrderHistory(String order_id, String name, String status, String date, String products, String total) {
        this.order_id = order_id;
        this.name = name;
        this.status = status;
        this.date = date;
        this.products = products;
        this.total = total;
    }

    public String getOrder_id() {
        return order_id;
    }

    public String getName() {
        return name;
    }

    public String getStatus() {
        return status;
    }

    public String getDate() {
        return date;
    }

    public String getProducts() {
        return products;
    }

    public String getTotal() {
        return total;
    }
}
