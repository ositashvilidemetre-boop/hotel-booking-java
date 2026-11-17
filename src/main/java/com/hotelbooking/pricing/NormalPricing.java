package com.hotelbooking.pricing;

import com.hotelbooking.model.Room;
import java.time.LocalDate;

public class NormalPricing implements PricingStrategy {

    @Override
    public double calculateTotal(Room room, LocalDate checkIn, LocalDate checkOut) {
        return countNights(checkIn, checkOut) * room.getNightlyPrice();
    }

    @Override
    public String getName() {
        return "Normal pricing";
    }
}