package com.app.flashcards.factory.flashcard;

import com.app.flashcards.dto.request.FlashcardCreateDtoRequest;
import com.app.flashcards.entity.Flashcard;
import com.app.flashcards.models.FlashcardCreationData;
import org.springframework.stereotype.Component;

@Component
public class FlashcardFactoryImpl implements FlashcardFactory {

    @Override
    public Flashcard createFromRequestAndData(FlashcardCreateDtoRequest createDtoRequest, FlashcardCreationData data) {
        return new Flashcard(createDtoRequest.getName(), createDtoRequest.getDefinition(), data.imageUrl(), data.cardFolder());
    }
}
