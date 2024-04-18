package com.app.flashcards.exception.custom;

public class SignUpException extends RuntimeException {
    public SignUpException(String message) {
        super(message);
    }
}
