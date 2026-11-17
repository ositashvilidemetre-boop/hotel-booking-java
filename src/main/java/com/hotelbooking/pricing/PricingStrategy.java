package com.hotelbooking.pricing;

import com.hotelbooking.model.Room;
import java.time.LocalDate;
import java.time.temporal.ChronoUnit;

public interface PricingStrategy {

    double calculateTotal(Room room, LocalDate checkIn, LocalDate checkOut);

    String getName();

    default long countNights(LocalDate checkIn, LocalDate checkOut) {
        if (checkIn == null || checkOut == null || !checkOut.isAfter(checkIn)) {
            throw new IllegalArgumentException("Check-out must be after check-in");
        }
        return ChronoUnit.DAYS.between(checkIn, checkOut);
    }
}