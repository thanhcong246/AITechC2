package com.vn.tcshop.aitechc.Models;

public class CartDetail {
    private final int id_cart_detail;
    private final int code_cart;
    private final String image_product;
    private final String name_product;
    private final String cost_product;
    private final String name_user;


    public CartDetail(int id_cart_detail, int code_cart, String image_product, String name_product, String cost_product, String name_user) {
        this.id_cart_detail = id_cart_detail;
        this.code_cart = code_cart;
        this.image_product = image_product;
        this.name_product = name_product;
        this.cost_product = cost_product;
        this.name_user = name_user;
    }


    public String getImage_product() {
        return image_product;
    }

    public String getName_product() {
        return name_product;
    }

    public String getCost_product() {
        return cost_product;
    }

    public String getName_user() {
        return name_user;
    }

    public int getCode_cart() {
        return code_cart;
    }

    public int getId_cart_detail() {
        return id_cart_detail;
    }
}