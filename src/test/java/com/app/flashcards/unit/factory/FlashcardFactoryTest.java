package com.app.flashcards.unit.factory;

import com.app.flashcards.dto.request.FlashcardCreateDtoRequest;
import com.app.flashcards.entity.Flashcard;
import com.app.flashcards.factory.flashcard.FlashcardFactory;
import com.app.flashcards.factory.flashcard.FlashcardFactoryImpl;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import static org.assertj.core.api.Assertions.assertThat;

class FlashcardFactoryTest {

    private FlashcardFactory flashcardFactory;

    @BeforeEach
    void setUp() {
        flashcardFactory = new FlashcardFactoryImpl();
    }

    @Test
    void createFlashcard() {
        FlashcardCreateDtoRequest createDtoRequest = new FlashcardCreateDtoRequest(1L, 1L,  "test-name", "test-definition", null);

        Flashcard flashcard = flashcardFactory.createFromDtoRequest(createDtoRequest);

        assertThat(flashcard.getName()).isEqualTo(createDtoRequest.getName());
        assertThat(flashcard.getDefinition()).isEqualTo(createDtoRequest.getDefinition());
    }
}
