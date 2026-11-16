package com.hotelbooking.model;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

public class Hotel {
    private final String name;
    private final String city;
    private final double rating;
    private final List<Room> rooms = new ArrayList<>();

    public Hotel(String name, String city, double rating) {
        if (name == null || name.isBlank()) {
            throw new IllegalArgumentException("Hotel name must not be empty");
        }
        if (city == null || city.isBlank()) {
            throw new IllegalArgumentException("City must not be empty");
        }
        if (rating < 0 || rating > 10) {
            throw new IllegalArgumentException("Rating must be between 0 and 10");
        }
        this.name = name;
        this.city = city;
        this.rating = rating;
    }

    public void addRoom(Room room) {
        if (room == null) {
            throw new IllegalArgumentException("Room must not be null");
        }
        rooms.add(room);
    }

    public String getName() { return name; }
    public String getCity() { return city; }
    public double getRating() { return rating; }

    public List<Room> getRooms() {
        return Collections.unmodifiableList(rooms);
    }

    @Override
    public String toString() {
        return name + " (" + city + ", rating " + rating + ")";
    }
}