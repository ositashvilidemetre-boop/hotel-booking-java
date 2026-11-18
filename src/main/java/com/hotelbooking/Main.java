package com.hotelbooking;

import com.hotelbooking.exception.InvalidInputException;
import com.hotelbooking.ui.InputReader;

import java.util.Scanner;

public class Main {
    public static void main(String[] args) {
        InputReader in = new InputReader(new Scanner(System.in));
        try {
            int n = in.readInt("Number of guests: ");
            System.out.println("OK: " + n);
            System.out.println("Date: " + in.readDate("Check-in"));
        } catch (InvalidInputException e) {
            System.out.println("Error: " + e.getMessage());
        }
    }
}