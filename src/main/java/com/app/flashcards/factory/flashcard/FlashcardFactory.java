package com.app.flashcards.factory.flashcard;

import com.app.flashcards.dto.request.FlashcardCreateDtoRequest;
import com.app.flashcards.entity.Flashcard;
public interface FlashcardFactory {

    Flashcard createFromDtoRequest(FlashcardCreateDtoRequest createDtoRequest);
}
