package com.vn.tcshop.aitechc.Models;

public class Shipping {
    private final int id_shipping;
    private final String name;
    private final String phone;
    private final String address;
    private final String note;
    private final int id_user;

    public Shipping(int id_shipping, String name, String phone, String address, String note, int id_user) {
        this.id_shipping = id_shipping;
        this.name = name;
        this.phone = phone;
        this.address = address;
        this.note = note;
        this.id_user = id_user;
    }

    public int getId_shipping() {
        return id_shipping;
    }

    public String getName() {
        return name;
    }

    public String getPhone() {
        return phone;
    }

    public String getAddress() {
        return address;
    }

    public String getNote() {
        return note;
    }

    public int getId_user() {
        return id_user;
    }
}
