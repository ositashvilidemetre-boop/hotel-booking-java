package com.hotelbooking.model;

public class DeluxeRoom extends Room {
    private final boolean breakfastIncluded;

    public DeluxeRoom(String roomNumber, int capacity, double basePrice, boolean breakfastIncluded) {
        super(roomNumber, capacity, basePrice);
        this.breakfastIncluded = breakfastIncluded;
    }

    public boolean isBreakfastIncluded() { return breakfastIncluded; }

    @Override
    public String getType() {
        return "Deluxe";
    }

    @Override
    public double getNightlyPrice() {
        double price = getBasePrice() * 1.25;
        if (breakfastIncluded) {
            price += 15;
        }
        return price;
    }
}