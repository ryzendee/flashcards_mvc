package com.app.flashcards.factory.flashcard;

import com.app.flashcards.dto.request.FlashcardCreateDtoRequest;
import com.app.flashcards.entity.Flashcard;
import org.springframework.stereotype.Component;

@Component
public class FlashcardFactoryImpl implements FlashcardFactory {

    @Override
    public Flashcard createFromDtoRequest(FlashcardCreateDtoRequest createDtoRequest) {
        return new Flashcard(createDtoRequest.getName(), createDtoRequest.getDefinition());
    }
}
