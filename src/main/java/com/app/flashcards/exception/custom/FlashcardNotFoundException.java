package com.app.flashcards.exception.custom;

public class FlashcardNotFoundException extends RuntimeException {
    public FlashcardNotFoundException(String message) {
        super(message);
    }
}
