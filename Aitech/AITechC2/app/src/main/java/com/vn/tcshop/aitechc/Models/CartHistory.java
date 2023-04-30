package com.vn.tcshop.aitechc.Models;

public class CartHistory {
    private final String code_cart;
    private final int cart_status;
    private final String cart_date;
    private final int cart_number;
    private final double cart_cost;
    private final String cart_payment;

    public CartHistory(String code_cart, int cart_status, String cart_date, int cart_number, double cart_cost, String cart_payment) {
        this.code_cart = code_cart;
        this.cart_status = cart_status;
        this.cart_date = cart_date;
        this.cart_number = cart_number;
        this.cart_cost = cart_cost;
        this.cart_payment = cart_payment;
    }

    public String getCode_cart() {
        return code_cart;
    }

    public int getCart_status() {
        return cart_status;
    }

    public String getCart_date() {
        return cart_date;
    }

    public int getCart_number() {
        return cart_number;
    }

    public double getCart_cost() {
        return cart_cost;
    }

    public String getCart_payment() {
        return cart_payment;
    }
}