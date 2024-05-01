package com.app.flashcards.models;

import com.app.flashcards.entity.CardFolder;

public record FlashcardCreationData(CardFolder cardFolder, String imageUrl) {
}
