package com.niki.eorder;

import com.niki.eorder.model.Cart;
import com.niki.eorder.model.History;

import java.util.ArrayList;

public class DataPassing {
    private static DataPassing instance;
    private String location;
    private String standID;
    private int seatNumber;
    private History history;
    private ArrayList<Cart> carts = new ArrayList<>();

    public History getHistory() {
        return history;
    }

    public void setHistory(History history) {
        this.history = history;
    }

    public ArrayList<Cart> getCarts() {
        return carts;
    }

    public void setCarts(ArrayList<Cart> carts) {
        this.carts = carts;
    }

    public String getLocation() {
        return location;
    }

    public void setLocation(String location) {
        this.location = location;
    }

    public String getStandID() {
        return standID;
    }

    public void setStandID(String standID) {
        this.standID = standID;
    }

    public int getSeatNumber() {
        return seatNumber;
    }

    public void setSeatNumber(int seatNumber) {
        this.seatNumber = seatNumber;
    }

    public static DataPassing getInstance(){
        if (instance == null){
            instance = new DataPassing();
        }
        return instance;
    }
}
