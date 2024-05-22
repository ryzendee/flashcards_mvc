package com.app.flashcards.integration.service;

import com.app.flashcards.dto.request.FlashcardCreateDtoRequest;
import com.app.flashcards.dto.request.FlashcardUpdateDtoRequest;
import com.app.flashcards.dto.response.FlashcardDtoResponse;
import com.app.flashcards.entity.CardFolder;
import com.app.flashcards.entity.Flashcard;
import com.app.flashcards.entity.User;
import com.app.flashcards.exception.cardfolder.CardFolderNotFoundException;
import com.app.flashcards.exception.flashcard.FlashcardCreateException;
import com.app.flashcards.exception.flashcard.FlashcardNotFoundException;
import com.app.flashcards.exception.flashcard.FlashcardUpdateException;
import com.app.flashcards.integration.ITBase;
import com.app.flashcards.repository.CardFolderRepository;
import com.app.flashcards.repository.FlashcardRepository;
import com.app.flashcards.repository.UserRepository;
import com.app.flashcards.service.flashcard.FlashcardService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.Arguments;
import org.junit.jupiter.params.provider.MethodSource;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;

import java.util.List;
import java.util.Optional;
import java.util.stream.Stream;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;

class FlashcardServiceIT extends ITBase {

    private static final Long ID = 1L;

    @Autowired
    private FlashcardService flashcardService;
    @Autowired
    private FlashcardRepository flashcardRepository;
    @Autowired
    private CardFolderRepository cardFolderRepository;
    @Autowired
    private UserRepository userRepository;

    @BeforeEach
    void setUp() {
        flashcardRepository.deleteAll();
        cardFolderRepository.deleteAll();
        userRepository.deleteAll();
    }

    @Test
    void getListByCardFolderId_existsId_returnsListWithCards() {
        //given
        User user = saveAndGetUser();
        CardFolder cardFolder = saveAndGetCardFolder(user);

        int expectedSize = 5;
        for (int i = 0; i < expectedSize; i++) {
            saveAndGetFlashcard(cardFolder);
        }

        //when
        List<FlashcardDtoResponse> flashcardsList = flashcardService.getListByFolderId(cardFolder.getId());

        //then
        assertThat(flashcardsList).hasSize(expectedSize);
    }

    @Test
    void getPageByCardFolderId_existId_returnsPageWithExpectedParams() {
        //given
        User user = saveAndGetUser();
        CardFolder cardFolder = saveAndGetCardFolder(user);

        int expectedTotalElements = 10;
        for (int i = 0; i < expectedTotalElements; i++) {
            saveAndGetFlashcard(cardFolder);
        }

        int page = 0;
        int size = 3;

        //when
        Page<FlashcardDtoResponse> flashcardsPage = flashcardService.getPageByFolderId(cardFolder.getId(), page, size);

        //then
        assertThat(flashcardsPage.getTotalElements()).isEqualTo(expectedTotalElements);
        assertThat(flashcardsPage).hasSize(size);
    }

    @Test
    void createFlashcard_nonExistsCardFolder_throwCardFolderNotFoundEx() {
        FlashcardCreateDtoRequest createDtoRequest = new FlashcardCreateDtoRequest();
        createDtoRequest.setFolderId(ID);

        assertThatThrownBy(() -> flashcardService.createFlashcard(createDtoRequest))
                .isInstanceOf(CardFolderNotFoundException.class);
    }

    @Test
    void createFlashcard_existsCardFolder_shouldCreateFlashcard() {
        //given
        User user = saveAndGetUser();
        CardFolder cardFolder = saveAndGetCardFolder(user);

        FlashcardCreateDtoRequest createDtoRequest = new FlashcardCreateDtoRequest(
                user.getId(),
                cardFolder.getId(),
                "created-name",
                "created-definition",
                null
        );

        //when
        flashcardService.createFlashcard(createDtoRequest);

        //then
        Optional<Flashcard> optionalFlashcard = flashcardRepository.findAll().stream()
                .findFirst();
        assertThat(optionalFlashcard).isPresent();

        Flashcard savedFlashcard = optionalFlashcard.get();
        assertThat(savedFlashcard.getName()).isEqualTo(createDtoRequest.getName());
        assertThat(savedFlashcard.getDefinition()).isEqualTo(createDtoRequest.getDefinition());
    }

    @MethodSource("getInvalidArgsForSavingEntity")
    @ParameterizedTest
    void createFlashcards_invalidDto_throwFlashcardCreateEx(String name, String definition) {
        //given
        User user = saveAndGetUser();
        CardFolder cardFolder = saveAndGetCardFolder(user);

        FlashcardCreateDtoRequest createDtoRequest = new FlashcardCreateDtoRequest();
        createDtoRequest.setFolderId(cardFolder.getId());
        createDtoRequest.setName(name);
        createDtoRequest.setDefinition(definition);

        //when
        //then
        assertThatThrownBy(() -> flashcardService.createFlashcard(createDtoRequest))
                .isInstanceOf(FlashcardCreateException.class);
    }

    @MethodSource("getInvalidArgsForSavingEntity")
    @ParameterizedTest
    void updateFlashcard_invalidDto_throwFlashcardUpdateEx(String invalidName, String invalidDefinition) {
        //given
        User user = saveAndGetUser();
        CardFolder cardFolder = saveAndGetCardFolder(user);
        Flashcard flashcard = saveAndGetFlashcard(cardFolder);

        FlashcardUpdateDtoRequest updateDtoRequest = new FlashcardUpdateDtoRequest();
        updateDtoRequest.setName(invalidName);
        updateDtoRequest.setDefinition(invalidDefinition);
        updateDtoRequest.setId(flashcard.getId());
        updateDtoRequest.setUserId(user.getId());
        updateDtoRequest.setFolderId(cardFolder.getId());

        //when
        //then
        assertThatThrownBy(() -> flashcardService.updateFlashcard(updateDtoRequest))
                .isInstanceOf(FlashcardUpdateException.class);
    }


    @Test
    void updateFlashcard_existsFlashcard_shouldUpdate() {
        //given
        User user = saveAndGetUser();
        CardFolder cardFolder = saveAndGetCardFolder(user);
        Flashcard flashcard = saveAndGetFlashcard(cardFolder);
        String entityImagePath = flashcard.getImagePath();


        FlashcardUpdateDtoRequest flashcardUpdateDtoRequest = new FlashcardUpdateDtoRequest(
                flashcard.getId(),
                user.getId(),
                cardFolder.getId(),
                "updated-dto-name",
                "updated-dto-definition",
                null
        );

        //when
        flashcardService.updateFlashcard(flashcardUpdateDtoRequest);

        //then
        assertThat(flashcardRepository.findById(flashcard.getId())).isPresent();

        Flashcard updatedEntity = flashcardRepository.findById(flashcard.getId()).get();
        assertThat(updatedEntity.getName()).isEqualTo(flashcardUpdateDtoRequest.getName());
        assertThat(updatedEntity.getDefinition()).isEqualTo(flashcardUpdateDtoRequest.getDefinition());
        // must not be changed
        // because entity already has image (imagePath) and image in request is null
        assertThat(updatedEntity.getImagePath()).isEqualTo(entityImagePath);
    }

    static Stream<Arguments> getInvalidArgsForSavingEntity() {
        String validName = "test-name";
        String validDefinition = "test-definition";

        return Stream.of(
                //Scenario 1: Null name
                Arguments.of(null, validDefinition),

                //Scenario 2: Null definition
                Arguments.of(validName, null)
        );
    }

    @Test
    void updateFlashcard_nonExistsFlashcard_throwFlashcardNotFoundEx() {
        FlashcardUpdateDtoRequest flashcardUpdateDtoRequest = new FlashcardUpdateDtoRequest(1L, 1L, 1L, "test-dto-name", "test-dto-definition", null);

        assertThatThrownBy(() -> flashcardService.updateFlashcard(flashcardUpdateDtoRequest))
                .isInstanceOf(FlashcardNotFoundException.class);
    }

    @Test
    void deleteById_existsFlashcard_shouldDelete() {
        //given
        User user = saveAndGetUser();
        CardFolder cardFolder = saveAndGetCardFolder(user);
        Flashcard flashcard = saveAndGetFlashcard(cardFolder);

        //when
        flashcardService.deleteFlashcardById(flashcard.getId());

        //then
        assertThat(flashcardRepository.findAll()).isEmpty();
    }

    private User saveAndGetUser() {
        User user = new User();
        user.setUsername("test-username");
        user.setPassword("test-password");
        return userRepository.save(user);
    }

    private CardFolder saveAndGetCardFolder(User user) {
        CardFolder cardFolder = new CardFolder();
        cardFolder.setUser(user);
        cardFolder.setName("test-folder-name");
        cardFolder.setDescription("test-folder-descr");
        return cardFolderRepository.save(cardFolder);
    }

    private Flashcard saveAndGetFlashcard(CardFolder cardFolder) {
        Flashcard flashcard = new Flashcard();
        flashcard.setCardFolder(cardFolder);
        flashcard.setName("test-name");
        flashcard.setDefinition("test-definition");
        flashcard.setImagePath("/test-image.jpg");
        return flashcardRepository.save(flashcard);
    }
}
