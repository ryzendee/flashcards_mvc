package com.app.flashcards.mapper.flashcard;

import com.app.flashcards.dto.response.FlashcardDtoResponse;
import com.app.flashcards.entity.Flashcard;
import org.springframework.stereotype.Component;

@Component
public class FlashcardMapperImpl implements FlashcardMapper {

    @Override
    public FlashcardDtoResponse toDto(Flashcard entity) {
        Long folderId = entity.getCardFolder().getId();

        return new FlashcardDtoResponse(
                entity.getId(),
                folderId,
                entity.getName(),
                entity.getDefinition(),
                entity.getImageUrl()
        );
    }
}
