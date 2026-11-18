package com.hotelbooking.persistence;

import com.hotelbooking.service.BookingService;

import java.io.IOException;

public interface DataStorage {
    void save(BookingService service) throws IOException;

    void load(BookingService service) throws IOException;
}