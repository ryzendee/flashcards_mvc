package com.app.flashcards.unit.factory;

import com.app.flashcards.dto.request.CardFolderCreateDtoRequest;
import com.app.flashcards.entity.CardFolder;
import com.app.flashcards.entity.User;
import com.app.flashcards.factory.cardfolder.CardFolderFactory;
import com.app.flashcards.factory.cardfolder.CardFolderFactoryImpl;
import com.app.flashcards.models.CardFolderCreationData;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import static org.assertj.core.api.Assertions.assertThat;

public class CardFolderFactoryTest {

    private CardFolderFactory cardFolderFactory;

    @BeforeEach
    void setUp() {
        cardFolderFactory = new CardFolderFactoryImpl();
    }

    @Test
    void test() {
        CardFolderCreateDtoRequest request = new CardFolderCreateDtoRequest("test-name", "test-descr", null);

        User user = new User();
        CardFolderCreationData creationData = new CardFolderCreationData(user, "test-imageUrl");

        CardFolder createdFolder = cardFolderFactory.createFromRequest(request, creationData);

        assertThat(createdFolder.getName()).isEqualTo(request.getName());
        assertThat(createdFolder.getDescription()).isEqualTo(request.getDescription());
        assertThat(createdFolder.getUser()).isEqualTo(creationData.user());
    }
}
