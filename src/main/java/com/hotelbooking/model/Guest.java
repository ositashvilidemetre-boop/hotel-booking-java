package com.hotelbooking.model;

public class Guest {
    private final String name;
    private final String email;

    public Guest(String name, String email) {
        if (name == null || name.isBlank()) {
            throw new IllegalArgumentException("Guest name must not be empty");
        }
        if (email == null || !email.contains("@")) {
            throw new IllegalArgumentException("Guest email must contain @");
        }
        this.name = name;
        this.email = email;
    }

    public String getName() { return name; }
    public String getEmail() { return email; }

    @Override
    public String toString() {
        return name + " <" + email + ">";
    }
}