package com.niki.eorder.model;

import java.io.Serializable;

public class Cart implements Serializable {
    private String locationID;
    private String standID;
    private String ID;
    private String name;
    private int qty;
    private int price;

    public String getStandID() {
        return standID;
    }

    public void setStandID(String standID) {
        this.standID = standID;
    }

    public String getLocationID() {
        return locationID;
    }

    public void setLocationID(String locationID) {
        this.locationID = locationID;
    }

    public String getID() {
        return ID;
    }

    public void setID(String ID) {
        this.ID = ID;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public int getQty() {
        return qty;
    }

    public void setQty(int qty) {
        this.qty = qty;
    }

    public int getPrice() {
        return price;
    }

    public void setPrice(int price) {
        this.price = price;
    }
}
