package com.app.flashcards.mapper.flashcard;

import com.app.flashcards.dto.response.FlashcardDtoResponse;
import com.app.flashcards.entity.Flashcard;

public interface FlashcardMapper {

    FlashcardDtoResponse toDto(Flashcard entity);
}
