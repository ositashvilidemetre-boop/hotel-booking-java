package com.hotelbooking;

import com.hotelbooking.model.*;
import java.util.ArrayList;
import java.util.List;

public class Main {
    public static void main(String[] args) {
        List<Room> rooms = new ArrayList<>();
        rooms.add(new StandardRoom("101", 2, 80));
        rooms.add(new DeluxeRoom("201", 3, 80, true));
        rooms.add(new Suite("301", 4, 80, 2));

        for (Room room : rooms) {
            System.out.println(room);
        }
    }
}