package com.hotelbooking.ui;

import com.hotelbooking.exception.InvalidInputException;
import com.hotelbooking.exception.RoomNotAvailableException;
import com.hotelbooking.model.Booking;
import com.hotelbooking.model.Guest;
import com.hotelbooking.model.Room;
import com.hotelbooking.persistence.DataStorage;
import com.hotelbooking.pricing.LongStayDiscountPricing;
import com.hotelbooking.pricing.NormalPricing;
import com.hotelbooking.pricing.SeasonalPricing;
import com.hotelbooking.service.BookingService;

import java.io.IOException;
import java.time.LocalDate;
import java.util.Comparator;
import java.util.List;
import java.util.Scanner;

public class ConsoleUI {
    private final BookingService service;
    private final DataStorage storage;
    private final InputReader input;

    public ConsoleUI(BookingService service, DataStorage storage, Scanner scanner) {
        this.service = service;
        this.storage = storage;
        this.input = new InputReader(scanner);
    }

    public void start() {
        boolean running = true;
        while (running) {
            printMenu();
            try {
                int choice = input.readInt("Choose: ");
                switch (choice) {
                    case 1 -> listHotels();
                    case 2 -> searchRooms();
                    case 3 -> bookRoom();
                    case 4 -> listBookings();
                    case 5 -> cancelBooking();
                    case 6 -> changePricing();
                    case 0 -> running = false;
                    default -> System.out.println("Unknown option.");
                }
            } catch (InvalidInputException | IllegalArgumentException e) {
                System.out.println("Error: " + e.getMessage());
            } catch (RoomNotAvailableException e) {
                System.out.println("Not available: " + e.getMessage());
            }
        }
        try {
            storage.save(service);
            System.out.println("Data saved. Goodbye!");
        } catch (IOException e) {
            System.out.println("Could not save data: " + e.getMessage());
        }
    }

    private void printMenu() {
        System.out.println();
        System.out.println("=== Hotel Booking ===");
        System.out.println("1. List hotels");
        System.out.println("2. Search available rooms");
        System.out.println("3. Book a room");
        System.out.println("4. List bookings");
        System.out.println("5. Cancel a booking");
        System.out.println("6. Change pricing policy (now: " + service.getPricingStrategy().getName() + ")");
        System.out.println("0. Save and exit");
    }

    private void listHotels() {
        if (service.getHotels().isEmpty()) {
            System.out.println("No hotels yet.");
        }
        service.getHotels().forEach(h -> {
            System.out.println(h);
            h.getRooms().forEach(r -> System.out.println("   " + r));
        });
    }

    private Comparator<Room> askSorter() {
        System.out.println("Sort by: 1 = price (low to high), 2 = price (high to low)");
        int s = input.readInt("Choose: ");
        Comparator<Room> byPrice = Comparator.comparingDouble(Room::getNightlyPrice);
        return s == 2 ? byPrice.reversed() : byPrice;
    }

    private List<Room> searchRooms() {
        String city = input.readText("City: ");
        LocalDate in = input.readDate("Check-in");
        LocalDate out = input.readDate("Check-out");
        List<Room> rooms = service.searchAvailable(city, in, out, askSorter());
        if (rooms.isEmpty()) {
            System.out.println("No rooms available.");
        }
        rooms.forEach(r -> System.out.println("  " + r));
        return rooms;
    }

    private void bookRoom() throws RoomNotAvailableException {
        String roomNumber = input.readText("Room number to book: ");
        Room room = findRoom(roomNumber);
        String name = input.readText("Guest name: ");
        String email = input.readText("Guest email: ");
        LocalDate in = input.readDate("Check-in");
        LocalDate out = input.readDate("Check-out");
        Booking booking = service.book(new Guest(name, email), room, in, out);
        System.out.println("Booked: " + booking);
    }

    private Room findRoom(String roomNumber) {
        return service.getHotels().stream()
                .flatMap(h -> h.getRooms().stream())
                .filter(r -> r.getRoomNumber().equals(roomNumber))
                .findFirst()
                .orElseThrow(() -> new InvalidInputException("No room with number " + roomNumber));
    }

    private void listBookings() {
        if (service.getBookings().isEmpty()) {
            System.out.println("No bookings yet.");
        }
        service.getBookings().forEach(System.out::println);
    }

    private void cancelBooking() {
        String id = input.readText("Booking id (for example B1): ");
        service.cancel(id);
        System.out.println("Cancelled " + id);
    }

    private void changePricing() {
        System.out.println("1 = Normal, 2 = Seasonal, 3 = Long-stay discount");
        switch (input.readInt("Choose: ")) {
            case 1 -> service.setPricingStrategy(new NormalPricing());
            case 2 -> service.setPricingStrategy(new SeasonalPricing());
            case 3 -> service.setPricingStrategy(new LongStayDiscountPricing());
            default -> throw new InvalidInputException("Choose 1, 2 or 3");
        }
        System.out.println("Pricing is now: " + service.getPricingStrategy().getName());
    }
}