package com.hotelbooking;

import com.hotelbooking.model.*;
import java.time.LocalDate;

public class Main {
    public static void main(String[] args) {
        Hotel hotel = new Hotel("Grand Budapest", "Budapest", 8.7);
        hotel.addRoom(new StandardRoom("101", 2, 80));
        hotel.addRoom(new DeluxeRoom("201", 3, 80, true));
        hotel.addRoom(new Suite("301", 4, 80, 2));

        System.out.println(hotel);
        for (Room room : hotel.getRooms()) {
            System.out.println("  " + room);
        }

        Guest guest = new Guest("Anna", "anna@example.com");
        Room room = hotel.getRooms().get(1);
        LocalDate in = LocalDate.of(2026, 12, 20);
        LocalDate out = LocalDate.of(2026, 12, 23);
        Booking booking = new Booking("B1", guest, room, in, out, room.getNightlyPrice() * 3);

        System.out.println(booking);
        System.out.println("Overlaps 22-25 Dec? "
                + booking.overlaps(LocalDate.of(2026, 12, 22), LocalDate.of(2026, 12, 25)));
        System.out.println("Overlaps 23-25 Dec? "
                + booking.overlaps(LocalDate.of(2026, 12, 23), LocalDate.of(2026, 12, 25)));
    }
}