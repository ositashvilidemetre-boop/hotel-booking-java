package com.hotelbooking;

import com.hotelbooking.exception.RoomNotAvailableException;
import com.hotelbooking.model.*;
import com.hotelbooking.persistence.DataStorage;
import com.hotelbooking.persistence.TextFileStorage;
import com.hotelbooking.service.BookingService;

import java.io.IOException;
import java.nio.file.Path;
import java.time.LocalDate;

public class Main {
    public static void main(String[] args) throws IOException, RoomNotAvailableException {
        DataStorage storage = new TextFileStorage(Path.of("data.txt"));

        BookingService first = new BookingService();
        Hotel hotel = new Hotel("Grand Budapest", "Budapest", 8.7);
        hotel.addRoom(new StandardRoom("101", 2, 80));
        hotel.addRoom(new DeluxeRoom("201", 3, 80, true));
        first.addHotel(hotel);
        first.book(new Guest("Anna", "anna@example.com"), hotel.getRooms().get(1),
                LocalDate.of(2026, 12, 20), LocalDate.of(2026, 12, 23));
        storage.save(first);
        System.out.println("Saved.");

        BookingService second = new BookingService();
        storage.load(second);
        System.out.println("Loaded hotels: " + second.getHotels());
        System.out.println("Loaded bookings: " + second.getBookings());
    }
}