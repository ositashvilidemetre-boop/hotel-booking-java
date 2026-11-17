package com.hotelbooking.pricing;

import com.hotelbooking.model.Room;
import java.time.LocalDate;
import java.time.Month;

public class SeasonalPricing implements PricingStrategy {
    private static final double PEAK_MULTIPLIER = 1.30;

    @Override
    public double calculateTotal(Room room, LocalDate checkIn, LocalDate checkOut) {
        countNights(checkIn, checkOut); // validates the dates
        double total = 0;
        for (LocalDate night = checkIn; night.isBefore(checkOut); night = night.plusDays(1)) {
            double price = room.getNightlyPrice();
            if (isPeakSeason(night)) {
                price *= PEAK_MULTIPLIER;
            }
            total += price;
        }
        return total;
    }

    private boolean isPeakSeason(LocalDate date) {
        Month m = date.getMonth();
        return m == Month.JUNE || m == Month.JULY || m == Month.AUGUST || m == Month.DECEMBER;
    }

    @Override
    public String getName() {
        return "Seasonal pricing";
    }
}