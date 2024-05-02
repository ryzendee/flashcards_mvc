package com.app.flashcards.factory.flashcard;

import com.app.flashcards.dto.request.FlashcardCreateDtoRequest;
import com.app.flashcards.entity.Flashcard;
import com.app.flashcards.models.FlashcardCreationData;

public interface FlashcardFactory {

    Flashcard createFromRequestAndData(FlashcardCreateDtoRequest createDtoRequest, FlashcardCreationData data);
}