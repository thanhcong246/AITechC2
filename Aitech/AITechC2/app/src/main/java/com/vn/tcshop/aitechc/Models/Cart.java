package com.vn.tcshop.aitechc.Models;

public class Cart {
    private final int id_cart;
    private final String name_user;
    private final String code_cart;
    private final int cart_status;
    private final String cart_date;
    private final String cart_payment;
    private final int id_shipping;

    public Cart(int id_cart, String name_user, String code_cart, int cart_status, String cart_date, String cart_payment, int id_shipping) {
        this.id_cart = id_cart;
        this.name_user = name_user;
        this.code_cart = code_cart;
        this.cart_status = cart_status;
        this.cart_date = cart_date;
        this.cart_payment = cart_payment;
        this.id_shipping = id_shipping;
    }

    public int getId_cart() {
        return id_cart;
    }

    public String getName_user() {
        return name_user;
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

    public String getCart_payment() {
        return cart_payment;
    }

    public int getId_shipping() {
        return id_shipping;
    }
}
