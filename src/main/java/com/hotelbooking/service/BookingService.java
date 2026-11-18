package com.hotelbooking.service;

import com.hotelbooking.exception.RoomNotAvailableException;
import com.hotelbooking.model.Booking;
import com.hotelbooking.model.Guest;
import com.hotelbooking.model.Hotel;
import com.hotelbooking.model.Room;
import com.hotelbooking.pricing.NormalPricing;
import com.hotelbooking.pricing.PricingStrategy;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.List;

public class BookingService {
    private final List<Hotel> hotels = new ArrayList<>();
    private final List<Booking> bookings = new ArrayList<>();
    private PricingStrategy pricingStrategy = new NormalPricing();
    private int nextId = 1;

    public void addHotel(Hotel hotel) {
        if (hotel == null) {
            throw new IllegalArgumentException("Hotel must not be null");
        }
        hotels.add(hotel);
    }

    public List<Hotel> getHotels() {
        return Collections.unmodifiableList(hotels);
    }

    public List<Booking> getBookings() {
        return Collections.unmodifiableList(bookings);
    }

    // Strategy pattern: the service (context) only knows the interface.
    public void setPricingStrategy(PricingStrategy pricingStrategy) {
        if (pricingStrategy == null) {
            throw new IllegalArgumentException("Pricing strategy must not be null");
        }
        this.pricingStrategy = pricingStrategy;
    }

    public PricingStrategy getPricingStrategy() {
        return pricingStrategy;
    }

    public boolean isAvailable(Room room, LocalDate checkIn, LocalDate checkOut) {
        for (Booking booking : bookings) {
            if (booking.getRoom() == room && booking.overlaps(checkIn, checkOut)) {
                return false;
            }
        }
        return true;
    }

    // Hotels in a city, best rating first (Comparator + reversed()).
    public List<Hotel> searchHotels(String city) {
        List<Hotel> result = new ArrayList<>();
        for (Hotel hotel : hotels) {
            if (hotel.getCity().equalsIgnoreCase(city)) {
                result.add(hotel);
            }
        }
        result.sort(Comparator.comparingDouble(Hotel::getRating).reversed());
        return result;
    }

    // Free rooms in a city for the dates, sorted by the comparator the caller passes in.
    public List<Room> searchAvailable(String city, LocalDate checkIn, LocalDate checkOut,
                                      Comparator<Room> sorter) {
        if (checkIn == null || checkOut == null || !checkOut.isAfter(checkIn)) {
            throw new IllegalArgumentException("Check-out must be after check-in");
        }
        List<Room> result = new ArrayList<>();
        for (Hotel hotel : searchHotels(city)) {
            for (Room room : hotel.getRooms()) {
                if (isAvailable(room, checkIn, checkOut)) {
                    result.add(room);
                }
            }
        }
        result.sort(sorter);
        return result;
    }

    public Booking book(Guest guest, Room room, LocalDate checkIn, LocalDate checkOut)
            throws RoomNotAvailableException {
        if (guest == null || room == null) {
            throw new IllegalArgumentException("Guest and room are required");
        }
        if (checkIn == null || checkOut == null || !checkOut.isAfter(checkIn)) {
            throw new IllegalArgumentException("Check-out must be after check-in");
        }
        if (!isAvailable(room, checkIn, checkOut)) {
            throw new RoomNotAvailableException("Room " + room.getRoomNumber()
                    + " is not available from " + checkIn + " to " + checkOut);
        }
        double total = pricingStrategy.calculateTotal(room, checkIn, checkOut);
        Booking booking = new Booking("B" + nextId++, guest, room, checkIn, checkOut, total);
        bookings.add(booking);
        return booking;
    }

    public void cancel(String bookingId) {
        boolean removed = bookings.removeIf(b -> b.getId().equals(bookingId));
        if (!removed) {
            throw new IllegalArgumentException("No booking with id " + bookingId);
        }
    }
}