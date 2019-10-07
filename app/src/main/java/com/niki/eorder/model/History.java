package com.niki.eorder.model;

import com.google.firebase.Timestamp;

import java.util.HashMap;
import java.util.Map;

public class History {
    private String locationID, standID, date;
    private Timestamp dateAndTime;
    private int seatNumber;
    private long totalPrice, reservationID;

    private Map<String, Integer> menuOrdered = new HashMap<>();
    private Map<String, Integer> price = new HashMap<>();

    public Map<String, Integer> getPrice() {
        return price;
    }

    public void setPrice(Map<String, Integer> price) {
        this.price = price;
    }

    public Map<String, Integer> getMenuOrdered() {
        return menuOrdered;
    }

    public void setMenuOrdered(Map<String, Integer> menuOrdered) {
        this.menuOrdered = menuOrdered;
    }

    public String getDate() {
        return date;
    }

    public void setDate(String date) {
        this.date = date;
    }

    public Timestamp getDateAndTime() {
        return dateAndTime;
    }

    public void setDateAndTime(Timestamp dateAndTime) {
        this.dateAndTime = dateAndTime;
    }

    public long getReservationID() {
        return reservationID;
    }

    public void setReservationID(long reservationID) {
        this.reservationID = reservationID;
    }

    public String getLocationID() {
        return locationID;
    }

    public void setLocationID(String locationID) {
        this.locationID = locationID;
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

    public long getTotalPrice() {
        return totalPrice;
    }

    public void setTotalPrice(long totalPrice) {
        this.totalPrice = totalPrice;
    }
}
