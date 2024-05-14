package com.app.flashcards.unit.factory;

import com.app.flashcards.dto.request.CardFolderCreateDtoRequest;
import com.app.flashcards.entity.CardFolder;
import com.app.flashcards.factory.cardfolder.CardFolderFactory;
import com.app.flashcards.factory.cardfolder.CardFolderFactoryImpl;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import static org.assertj.core.api.Assertions.assertThat;

class CardFolderFactoryTest {

    private CardFolderFactory cardFolderFactory;

    @BeforeEach
    void setUp() {
        cardFolderFactory = new CardFolderFactoryImpl();
    }

    @Test
    void test() {
        CardFolderCreateDtoRequest request = new CardFolderCreateDtoRequest(1L, "test-name", "test-descr", null);


        CardFolder createdFolder = cardFolderFactory.createFromRequest(request);

        assertThat(createdFolder.getName()).isEqualTo(request.getName());
        assertThat(createdFolder.getDescription()).isEqualTo(request.getDescription());
    }
}
