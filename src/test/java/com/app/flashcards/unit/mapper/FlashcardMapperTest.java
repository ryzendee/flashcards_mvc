package com.app.flashcards.unit.mapper;

import com.app.flashcards.dto.response.FlashcardDtoResponse;
import com.app.flashcards.entity.CardFolder;
import com.app.flashcards.entity.Flashcard;
import com.app.flashcards.mapper.flashcard.FlashcardMapper;
import com.app.flashcards.mapper.flashcard.FlashcardMapperImpl;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import static org.assertj.core.api.Assertions.as;
import static org.assertj.core.api.Assertions.assertThat;

public class FlashcardMapperTest {

    private FlashcardMapper flashcardMapper;

    @BeforeEach
    void setUp() {
        flashcardMapper = new FlashcardMapperImpl();
    }

    @Test
    void toDto() {
        CardFolder cardFolder = new CardFolder();
        cardFolder.setId(1L);

        Flashcard entity = new Flashcard();
        entity.setCardFolder(cardFolder);
        entity.setId(1L);
        entity.setName("test-name");
        entity.setDefinition("test-def");
        entity.setImageUrl("test-url");

        FlashcardDtoResponse dtoResponse = flashcardMapper.toDto(entity);

        assertThat(dtoResponse.getFolderId()).isEqualTo(cardFolder.getId());
        assertThat(dtoResponse.getId()).isEqualTo(entity.getId());
        assertThat(dtoResponse.getName()).isEqualTo(entity.getName());
        assertThat(dtoResponse.getDefinition()).isEqualTo(entity.getDefinition());
        assertThat(dtoResponse.getImageUrl()).isEqualTo(entity.getImageUrl());
    }
}
