package com.hotelbooking.model;

public abstract class Room {
    private final String roomNumber;
    private final int capacity;
    private final double basePrice;

    protected Room(String roomNumber, int capacity, double basePrice) {
        if (roomNumber == null || roomNumber.isBlank()) {
            throw new IllegalArgumentException("Room number must not be empty");
        }
        if (capacity < 1) {
            throw new IllegalArgumentException("Capacity must be at least 1");
        }
        if (basePrice <= 0) {
            throw new IllegalArgumentException("Base price must be positive");
        }
        this.roomNumber = roomNumber;
        this.capacity = capacity;
        this.basePrice = basePrice;
    }

    public String getRoomNumber() { return roomNumber; }
    public int getCapacity() { return capacity; }
    public double getBasePrice() { return basePrice; }

    public abstract String getType();
    public abstract double getNightlyPrice();

    @Override
    public String toString() {
        return getType() + " " + roomNumber + " (" + capacity + " guests, "
                + String.format("%.2f", getNightlyPrice()) + " per night)";
    }
}