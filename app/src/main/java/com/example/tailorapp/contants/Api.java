package com.example.tailorapp.contants;

public class Api {

    private static String BaseURL = "http://110.93.225.221/tailor/index.php?route=feed/rest_api/";

    public static String LoginURL = BaseURL + "login&key=ssred1";
    public static String SignupURL = BaseURL + "signup&key=ssred1";
    public static String CategoriesURL = BaseURL + "categories&key=ssred1";
    public static String CategoryListURL = BaseURL + "products&key=ssred1";
    public static String ProductDetailURL = BaseURL + "product_detail&key=ssred1";
    public static String ShippingMethodsURL = BaseURL + "shipping_methods&key=ssred1";
    public static String OrderHistoryURL = BaseURL + "order_history&key=ssred1";
    public static String PlaceOrderURL = BaseURL + "place_order&key=ssred1";
}
