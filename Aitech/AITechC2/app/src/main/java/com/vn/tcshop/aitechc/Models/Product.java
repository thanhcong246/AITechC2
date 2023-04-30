package com.vn.tcshop.aitechc.Models;

import java.io.Serializable;

public class Product implements Serializable {
    private final int id;
    private final String name;
    private final String cost;
    private final String image;
    private final String detail;
    private final String category;
    private final float rate;

    public Product(int id, String name, String cost, String image, String detail, String category, float rate) {
        this.id = id;
        this.name = name;
        this.cost = cost;
        this.image = image;
        this.detail = detail;
        this.category = category;
        this.rate = rate;
    }

    public int getId() {
        return id;
    }

    public String getName() {
        return name;
    }

    public String getCost() {
        return cost;
    }

    public String getImage() { // Sửa kiểu dữ liệu trả về thành String
        return image;
    }

    public String getDetail() {
        return detail;
    }

    public String getCategory() {
        return category;
    }

    public float getRate(){return rate;}
}