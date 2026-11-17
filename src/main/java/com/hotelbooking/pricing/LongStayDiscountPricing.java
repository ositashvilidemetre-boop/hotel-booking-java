package com.hotelbooking.pricing;

import com.hotelbooking.model.Room;
import java.time.LocalDate;

public class LongStayDiscountPricing implements PricingStrategy {
    private static final int MIN_NIGHTS = 7;
    private static final double DISCOUNT = 0.10;

    @Override
    public double calculateTotal(Room room, LocalDate checkIn, LocalDate checkOut) {
        long nights = countNights(checkIn, checkOut);
        double total = nights * room.getNightlyPrice();
        if (nights >= MIN_NIGHTS) {
            total *= (1 - DISCOUNT);
        }
        return total;
    }

    @Override
    public String getName() {
        return "Long-stay discount";
    }
}