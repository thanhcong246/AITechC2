package com.vn.tcshop.aitechc.Models;

public class ProductComment {
    private final String userName;
    private final String productName;
    private final String comment;
    private final float rate;
    private final String date;

    public ProductComment(String userName, String productName, String comment, float rate, String date) {
        this.userName = userName;
        this.productName = productName;
        this.comment = comment;
        this.rate = rate;
        this.date = date;
    }

    public String getUserName() {
        return userName;
    }

    public String getProductName() {
        return productName;
    }

    public String getComment() {
        return comment;
    }

    public float getRate() {
        return rate;
    }

    public String getDate() {
        return date;
    }
}

