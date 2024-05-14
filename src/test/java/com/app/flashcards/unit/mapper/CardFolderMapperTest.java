package com.app.flashcards.unit.mapper;

import com.app.flashcards.dto.response.CardFolderDtoResponse;
import com.app.flashcards.entity.CardFolder;
import com.app.flashcards.mapper.cardfolder.CardFolderMapper;
import com.app.flashcards.mapper.cardfolder.CardFolderMapperImpl;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import static org.assertj.core.api.Assertions.assertThat;

public class CardFolderMapperTest {

    private CardFolderMapper cardFolderMapper;

    @BeforeEach
    void setUp() {
        cardFolderMapper = new CardFolderMapperImpl();
    }

    @Test
    void toDto() {
        CardFolder cardFolder = new CardFolder();
        cardFolder.setId(1L);
        cardFolder.setName("test-name");
        cardFolder.setDescription("test-descr");
        cardFolder.setImagePath("test-url");

        CardFolderDtoResponse dtoResponse = cardFolderMapper.toDto(cardFolder);

        assertThat(dtoResponse.getId()).isEqualTo(cardFolder.getId());
        assertThat(dtoResponse.getName()).isEqualTo(cardFolder.getName());
        assertThat(dtoResponse.getDescription()).isEqualTo(cardFolder.getDescription());
        assertThat(dtoResponse.getImageUrl()).isEqualTo(cardFolder.getImagePath());
    }
}
