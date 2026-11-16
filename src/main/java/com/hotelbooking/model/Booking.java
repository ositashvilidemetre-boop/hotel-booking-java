package com.hotelbooking.model;

import java.time.LocalDate;
import java.time.temporal.ChronoUnit;

public class Booking {
    private final String id;
    private final Guest guest;
    private final Room room;
    private final LocalDate checkIn;
    private final LocalDate checkOut;
    private final double totalPrice;

    public Booking(String id, Guest guest, Room room,
                   LocalDate checkIn, LocalDate checkOut, double totalPrice) {
        if (id == null || id.isBlank()) {
            throw new IllegalArgumentException("Booking id must not be empty");
        }
        if (guest == null || room == null) {
            throw new IllegalArgumentException("Guest and room are required");
        }
        if (checkIn == null || checkOut == null || !checkOut.isAfter(checkIn)) {
            throw new IllegalArgumentException("Check-out must be after check-in");
        }
        if (totalPrice < 0) {
            throw new IllegalArgumentException("Total price must not be negative");
        }
        this.id = id;
        this.guest = guest;
        this.room = room;
        this.checkIn = checkIn;
        this.checkOut = checkOut;
        this.totalPrice = totalPrice;
    }

    public String getId() { return id; }
    public Guest getGuest() { return guest; }
    public Room getRoom() { return room; }
    public LocalDate getCheckIn() { return checkIn; }
    public LocalDate getCheckOut() { return checkOut; }
    public double getTotalPrice() { return totalPrice; }

    public long getNights() {
        return ChronoUnit.DAYS.between(checkIn, checkOut);
    }

    public boolean overlaps(LocalDate otherIn, LocalDate otherOut) {
        return checkIn.isBefore(otherOut) && otherIn.isBefore(checkOut);
    }

    @Override
    public String toString() {
        return "Booking " + id + ": " + guest.getName() + ", room " + room.getRoomNumber()
                + ", " + checkIn + " to " + checkOut
                + ", total " + String.format("%.2f", totalPrice);
    }
}