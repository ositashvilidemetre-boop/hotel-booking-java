package com.hotelbooking.persistence;

import com.hotelbooking.model.*;
import com.hotelbooking.service.BookingService;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.time.LocalDate;
import java.util.HashMap;
import java.util.Map;

public class TextFileStorage implements DataStorage {
    private final Path file;

    public TextFileStorage(Path file) {
        this.file = file;
    }

    @Override
    public void save(BookingService service) throws IOException {
        try (BufferedWriter out = Files.newBufferedWriter(file)) {
            for (Hotel hotel : service.getHotels()) {
                out.write("HOTEL;" + hotel.getName() + ";" + hotel.getCity() + ";" + hotel.getRating());
                out.newLine();
                for (Room room : hotel.getRooms()) {
                    out.write("ROOM;" + hotel.getName() + ";" + roomToText(room));
                    out.newLine();
                }
            }
            for (Booking b : service.getBookings()) {
                out.write("BOOKING;" + b.getId() + ";" + b.getGuest().getName() + ";"
                        + b.getGuest().getEmail() + ";" + b.getRoom().getRoomNumber() + ";"
                        + b.getCheckIn() + ";" + b.getCheckOut() + ";" + b.getTotalPrice());
                out.newLine();
            }
        }
    }

    private String roomToText(Room room) {
        if (room instanceof DeluxeRoom d) {
            return "DELUXE;" + room.getRoomNumber() + ";" + room.getCapacity() + ";"
                    + room.getBasePrice() + ";" + d.isBreakfastIncluded();
        } else if (room instanceof Suite s) {
            return "SUITE;" + room.getRoomNumber() + ";" + room.getCapacity() + ";"
                    + room.getBasePrice() + ";" + s.getBedrooms();
        }
        return "STANDARD;" + room.getRoomNumber() + ";" + room.getCapacity() + ";"
                + room.getBasePrice();
    }

    @Override
    public void load(BookingService service) throws IOException {
        if (!Files.exists(file)) {
            return; // first run: nothing saved yet
        }
        Map<String, Hotel> hotels = new HashMap<>();
        try (BufferedReader in = Files.newBufferedReader(file)) {
            String line;
            while ((line = in.readLine()) != null) {
                if (line.isBlank()) {
                    continue;
                }
                String[] p = line.split(";");
                try {
                    switch (p[0]) {
                        case "HOTEL" -> {
                            Hotel h = new Hotel(p[1], p[2], Double.parseDouble(p[3]));
                            hotels.put(h.getName(), h);
                            service.addHotel(h);
                        }
                        case "ROOM" -> hotels.get(p[1]).addRoom(parseRoom(p));
                        case "BOOKING" -> service.restoreBooking(new Booking(p[1],
                                new Guest(p[2], p[3]), findRoom(service, p[4]),
                                LocalDate.parse(p[5]), LocalDate.parse(p[6]),
                                Double.parseDouble(p[7])));
                        default -> throw new IOException("Unknown record: " + p[0]);
                    }
                } catch (RuntimeException e) {
                    throw new IOException("Corrupt data line: " + line, e);
                }
            }
        }
    }

    private Room parseRoom(String[] p) {
        String number = p[3];
        int capacity = Integer.parseInt(p[4]);
        double price = Double.parseDouble(p[5]);
        return switch (p[2]) {
            case "DELUXE" -> new DeluxeRoom(number, capacity, price, Boolean.parseBoolean(p[6]));
            case "SUITE" -> new Suite(number, capacity, price, Integer.parseInt(p[6]));
            default -> new StandardRoom(number, capacity, price);
        };
    }

    private Room findRoom(BookingService service, String roomNumber) {
        for (Hotel h : service.getHotels()) {
            for (Room r : h.getRooms()) {
                if (r.getRoomNumber().equals(roomNumber)) {
                    return r;
                }
            }
        }
        throw new IllegalArgumentException("Unknown room " + roomNumber);
    }
}