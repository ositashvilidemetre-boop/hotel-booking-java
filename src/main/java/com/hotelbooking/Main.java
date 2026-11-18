package com.hotelbooking;

import com.hotelbooking.model.*;
import com.hotelbooking.persistence.DataStorage;
import com.hotelbooking.persistence.TextFileStorage;
import com.hotelbooking.service.BookingService;
import com.hotelbooking.ui.ConsoleUI;

import java.io.IOException;
import java.nio.file.Path;
import java.util.Scanner;

public class Main {
    public static void main(String[] args) {
        BookingService service = new BookingService();
        DataStorage storage = new TextFileStorage(Path.of("data.txt"));

        try {
            storage.load(service);
        } catch (IOException e) {
            System.out.println("Could not load saved data: " + e.getMessage());
        }

        if (service.getHotels().isEmpty()) {
            seedSampleData(service);
        }

        new ConsoleUI(service, storage, new Scanner(System.in)).start();
    }

    private static void seedSampleData(BookingService service) {
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
    }
}