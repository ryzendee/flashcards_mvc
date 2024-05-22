package com.app.flashcards.integration.service;

import com.app.flashcards.dto.request.CardFolderCreateDtoRequest;
import com.app.flashcards.dto.request.CardFolderUpdateDtoRequest;
import com.app.flashcards.dto.response.CardFolderDtoResponse;
import com.app.flashcards.entity.CardFolder;
import com.app.flashcards.entity.User;
import com.app.flashcards.enums.ImagePath;
import com.app.flashcards.exception.cardfolder.CardFolderCreateException;
import com.app.flashcards.exception.cardfolder.CardFolderNotFoundException;
import com.app.flashcards.exception.cardfolder.CardFolderUpdateException;
import com.app.flashcards.exception.user.UserNotFoundException;
import com.app.flashcards.integration.ITBase;
import com.app.flashcards.repository.CardFolderRepository;
import com.app.flashcards.repository.UserRepository;
import com.app.flashcards.service.cardfolder.CardFolderService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.NullSource;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;


class CardFolderServiceIT extends ITBase {

    private static final Long ID = 1L;

    @Autowired
    private CardFolderService cardFolderService;
    @Autowired
    private CardFolderRepository cardFolderRepository;
    @Autowired
    private UserRepository userRepository;


    @BeforeEach
    void setUp() {
        userRepository.deleteAll();
        cardFolderRepository.deleteAll();
    }

    @Test
    void createCardFolder_existsUser_shouldSave() {
        //given
        User user = saveAndGetUser();

        CardFolderCreateDtoRequest createDtoRequest = getCreateDtoRequestWithoutUserId();
        createDtoRequest.setUserId(user.getId());

        //when
        cardFolderService.createCardFolder(createDtoRequest);

        //then
        CardFolder actualCardFolder = cardFolderRepository.findAll().stream()
                .findFirst()
                .orElse(null);

        assertThat(actualCardFolder).isNotNull();
        assertThat(actualCardFolder.getName()).isEqualTo(createDtoRequest.getName());
        assertThat(actualCardFolder.getDescription()).isEqualTo(createDtoRequest.getDescription());
        assertThat(actualCardFolder.getImagePath()).isEqualTo(ImagePath.DEFAULT.getPathToImage());
    }

    @Test
    void createCardFolder_nonExistsUser_throwsUserNotFoundException() {
        CardFolderCreateDtoRequest cardFolderCreateDtoRequest = getCreateDtoRequestWithoutUserId();
        cardFolderCreateDtoRequest.setUserId(ID);

        assertThatThrownBy(() -> cardFolderService.createCardFolder(cardFolderCreateDtoRequest))
                .isInstanceOf(UserNotFoundException.class);
    }

    @NullSource
    @ParameterizedTest
    void createCardFolder_invalidDtoName_throwCardFolderCreateEx(String name) {
        User user = saveAndGetUser();
        CardFolderCreateDtoRequest createDtoRequest = new CardFolderCreateDtoRequest(user.getId(), name, "test-descr", null);

        assertThatThrownBy(() -> cardFolderService.createCardFolder(createDtoRequest))
                .isInstanceOf(CardFolderCreateException.class);
    }

    @NullSource
    @ParameterizedTest
    void updateCardFolder_invalidDtoName_throwsCardFolderUpdateEx(String name) {
        User user = saveAndGetUser();
        CardFolder cardFolder = saveAndGetCardFolder(user);
        CardFolderUpdateDtoRequest updateDtoRequest = new CardFolderUpdateDtoRequest(cardFolder.getId(), user.getId(), name, "test-descr", null);

        assertThatThrownBy(() -> cardFolderService.updateCardFolder(updateDtoRequest))
                .isInstanceOf(CardFolderUpdateException.class);
    }

    @Test
    void existsByUserIdAndId_existsBothIds_returnsTrue() {
        //given
        User user = saveAndGetUser();
        CardFolder cardFolder = saveAndGetCardFolder(user);

        //when
        boolean existsResult = cardFolderService.existsByIdAndUserId(cardFolder.getId(), user.getId());

        //then
        assertThat(existsResult).isTrue();
    }

    @Test
    void deleteById_existsIdAndEntity_shouldDelete() {
        //given
        User user = saveAndGetUser();
        CardFolder cardFolder = saveAndGetCardFolder(user);

        //when
        cardFolderService.deleteById(cardFolder.getId());

        //then
        assertThat(cardFolderRepository.findAll()).isEmpty();
    }

    @Test
    void getPageByCardFolderId_existId_returnsPageWithExpectedParams() {
        //given
        User user = saveAndGetUser();

        int expectedTotalElements = 10;
        for (int i = 0; i < expectedTotalElements; i++) {
            saveAndGetCardFolder(user);
        }

        int page = 0;
        int size = 3;

        //when
        Page<CardFolderDtoResponse> cardFoldersPage = cardFolderService.getCardFolderPageByUserId(user.getId(), page, size);

        //then
        assertThat(cardFoldersPage.getTotalElements()).isEqualTo(expectedTotalElements);
        assertThat(cardFoldersPage).hasSize(size);
    }

    @Test
    void getById_existsId_returnsCardFolder() {
        //given
        User user = saveAndGetUser();
        CardFolder cardFolder = saveAndGetCardFolder(user);

        //when
        CardFolder actualCardFolder = cardFolderService.getById(cardFolder.getId());

        //then
        assertThat(actualCardFolder).isNotNull();
    }

    @Test
    void getById_nonExistsId_throwCardFolderNotFoundEx() {
        assertThatThrownBy(() -> cardFolderService.getById(ID))
                .isInstanceOf(CardFolderNotFoundException.class);
    }

    private User saveAndGetUser() {
        User user = new User();
        user.setUsername("test-username");
        user.setPassword("test-password");

        return userRepository.save(user);
    }

    private CardFolderCreateDtoRequest getCreateDtoRequestWithoutUserId() {
        CardFolderCreateDtoRequest request = new CardFolderCreateDtoRequest();

        request.setName("test-dto-name");
        request.setDescription("test-dto-descr");

        return request;
    }

    private CardFolder saveAndGetCardFolder(User user) {
        CardFolder cardFolder = new CardFolder();
        cardFolder.setUser(user);
        cardFolder.setName("test-entity-name");
        cardFolder.setDescription("test-entity-descr");

        return cardFolderRepository.save(cardFolder);
    }
}
