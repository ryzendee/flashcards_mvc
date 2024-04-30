package com.app.flashcards.exception.custom;

public class CardFolderNotFoundException extends RuntimeException {

    public CardFolderNotFoundException(String message) {
        super(message);
    }
}
