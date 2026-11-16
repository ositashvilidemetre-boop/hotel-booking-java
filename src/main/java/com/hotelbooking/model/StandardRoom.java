package com.hotelbooking.model;

public class StandardRoom extends Room {

    public StandardRoom(String roomNumber, int capacity, double basePrice) {
        super(roomNumber, capacity, basePrice);
    }

    @Override
    public String getType() {
        return "Standard";
    }

    @Override
    public double getNightlyPrice() {
        return getBasePrice();
    }
}