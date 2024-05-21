package com.app.flashcards.exception.cardfolder;

public class CardFolderNotFoundException extends RuntimeException {

    public CardFolderNotFoundException(String message) {
        super(message);
    }
}
