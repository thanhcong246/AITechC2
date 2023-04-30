package com.vn.tcshop.aitechc.Models;

public class Payment {
    private final String name_product_payment;
    private final String cost_product_payment;
    private final String image_product_payment;

    public Payment(String name_product_payment, String cost_product_payment, String image_product_payment) {
        this.name_product_payment = name_product_payment;
        this.cost_product_payment = cost_product_payment;
        this.image_product_payment = image_product_payment;
    }

    public String getName_product_payment() {
        return name_product_payment;
    }

    public String getCost_product_payment() {
        return cost_product_payment;
    }

    public String getImage_product_payment() {
        return image_product_payment;
    }
}