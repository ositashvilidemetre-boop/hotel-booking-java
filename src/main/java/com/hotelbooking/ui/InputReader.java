package com.hotelbooking.ui;

import com.hotelbooking.exception.InvalidInputException;

import java.time.LocalDate;
import java.time.format.DateTimeParseException;
import java.util.Scanner;

public class InputReader {
    private final Scanner scanner;

    public InputReader(Scanner scanner) {
        this.scanner = scanner;
    }

    public String readText(String prompt) {
        System.out.print(prompt);
        String line = scanner.nextLine().trim();
        if (line.isEmpty()) {
            throw new InvalidInputException("Input must not be empty");
        }
        return line;
    }

    public int readInt(String prompt) {
        String line = readText(prompt);
        try {
            return Integer.parseInt(line);
        } catch (NumberFormatException e) {
            throw new InvalidInputException("'" + line + "' is not a whole number");
        }
    }

    public LocalDate readDate(String prompt) {
        String line = readText(prompt + " (yyyy-mm-dd): ");
        try {
            return LocalDate.parse(line);
        } catch (DateTimeParseException e) {
            throw new InvalidInputException("'" + line + "' is not a valid date, use yyyy-mm-dd");
        }
    }
}