package com.hotelbooking;

import com.hotelbooking.exception.RoomNotAvailableException;
import com.hotelbooking.model.*;
import com.hotelbooking.pricing.SeasonalPricing;
import com.hotelbooking.service.BookingService;

import java.time.LocalDate;
import java.util.Comparator;

public class Main {
    public static void main(String[] args) {
        BookingService service = new BookingService();

        Hotel grand = new Hotel("Grand Budapest", "Budapest", 8.7);
        grand.addRoom(new StandardRoom("101", 2, 80));
        grand.addRoom(new DeluxeRoom("201", 3, 80, true));
        grand.addRoom(new Suite("301", 4, 80, 2));

        Hotel hostel = new Hotel("City Hostel Plus", "Budapest", 7.2);
        hostel.addRoom(new StandardRoom("11", 2, 50));
        hostel.addRoom(new StandardRoom("12", 2, 60));

        Hotel vienna = new Hotel("Vienna Palace", "Vienna", 9.1);
        vienna.addRoom(new Suite("1", 2, 120, 1));

        service.addHotel(grand);
        service.addHotel(hostel);
        service.addHotel(vienna);

        LocalDate in = LocalDate.of(2026, 12, 20);
        LocalDate out = LocalDate.of(2026, 12, 23);
        Comparator<Room> byPrice = Comparator.comparingDouble(Room::getNightlyPrice);

        System.out.println("Available in Budapest, 20-23 Dec, cheapest first:");
        for (Room room : service.searchAvailable("Budapest", in, out, byPrice)) {
            System.out.println("  " + room);
        }

        Guest anna = new Guest("Anna", "anna@example.com");
        Room deluxe = grand.getRooms().get(1);

        try {
            System.out.println(service.book(anna, deluxe, in, out));
            service.book(anna, deluxe, LocalDate.of(2026, 12, 22), LocalDate.of(2026, 12, 24));
        } catch (RoomNotAvailableException e) {
            System.out.println("Error: " + e.getMessage());
        }

        service.setPricingStrategy(new SeasonalPricing());
        try {
            System.out.println(service.book(anna, grand.getRooms().get(0), in, out));
        } catch (RoomNotAvailableException e) {
            System.out.println("Error: " + e.getMessage());
        }

        System.out.println("Hotels in Budapest by rating:");
        for (Hotel hotel : service.searchHotels("Budapest")) {
            System.out.println("  " + hotel);
        }
    }
}