package com.app.flashcards.enums;

public enum SessionAttributes {
    USER_ID("userId");

    private final String name;

    SessionAttributes(String name) {
        this.name = name;
    }

    public String getName() {
        return name;
    }
}
