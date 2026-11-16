package com.hotelbooking.model;

public class Suite extends Room {
    private final int bedrooms;

    public Suite(String roomNumber, int capacity, double basePrice, int bedrooms) {
        super(roomNumber, capacity, basePrice);
        if (bedrooms < 1) {
            throw new IllegalArgumentException("A suite needs at least 1 bedroom");
        }
        this.bedrooms = bedrooms;
    }

    public int getBedrooms() { return bedrooms; }

    @Override
    public String getType() {
        return "Suite";
    }

    @Override
    public double getNightlyPrice() {
        return getBasePrice() * 1.5 + bedrooms * 20;
    }
}