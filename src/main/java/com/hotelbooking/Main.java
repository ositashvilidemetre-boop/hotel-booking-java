package com.hotelbooking;

import com.hotelbooking.model.*;
import com.hotelbooking.pricing.*;
import java.time.LocalDate;
import java.util.List;

public class Main {
    public static void main(String[] args) {
        Room room = new DeluxeRoom("201", 3, 80, true); // 115.00 per night

        LocalDate shortIn = LocalDate.of(2026, 12, 20);
        LocalDate shortOut = LocalDate.of(2026, 12, 23);   // 3 nights
        LocalDate longIn = LocalDate.of(2027, 1, 10);
        LocalDate longOut = LocalDate.of(2027, 1, 17);     // 7 nights

        List<PricingStrategy> strategies = List.of(
                new NormalPricing(),
                new SeasonalPricing(),
                new LongStayDiscountPricing());

        for (PricingStrategy strategy : strategies) {
            System.out.println(strategy.getName()
                    + ": 3 nights = " + String.format("%.2f", strategy.calculateTotal(room, shortIn, shortOut))
                    + ", 7 nights = " + String.format("%.2f", strategy.calculateTotal(room, longIn, longOut)));
        }
    }
}