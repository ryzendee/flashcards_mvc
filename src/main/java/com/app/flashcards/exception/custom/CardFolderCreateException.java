package com.app.flashcards.exception.custom;

public class CardFolderCreateException extends RuntimeException{
    public CardFolderCreateException(String message) {
        super(message);
    }
}
