package com.app.flashcards.unit.service;

import com.app.flashcards.dto.request.CardFolderUpdateDtoRequest;
import com.app.flashcards.dto.request.FlashcardCreateDtoRequest;
import com.app.flashcards.dto.request.FlashcardUpdateDtoRequest;
import com.app.flashcards.entity.Flashcard;
import com.app.flashcards.exception.custom.FlashcardNotFoundException;
import com.app.flashcards.factory.flashcard.FlashcardFactory;
import com.app.flashcards.models.CardFolderUpdateData;
import com.app.flashcards.models.FlashcardCreationData;
import com.app.flashcards.models.FlashcardUpdateData;
import com.app.flashcards.repository.FlashcardRepository;
import com.app.flashcards.service.flashcard.FlashcardServiceImpl;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;

import java.util.List;
import java.util.Optional;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
public class FlashcardServiceTest {

    private static final Long ID = 1L;
    @InjectMocks
    private FlashcardServiceImpl flashcardService;

    @Mock
    private FlashcardRepository flashcardRepository;
    @Mock
    private FlashcardFactory flashcardFactory;

    @Test
    void getListByFolderId_existsFolderId_returnListWithEntity() {
        Flashcard entity = getFlashcardWithId();
        List<Flashcard> expectedEntityList = List.of(entity);

        when(flashcardRepository.findAllByCardFolderId(ID))
                .thenReturn(expectedEntityList);

        List<Flashcard> actualList = flashcardService.getListByFolderId(ID);

        assertThat(actualList).containsAll(expectedEntityList);
    }

    @Test
    void getPageByFolderId_withPageAndSize_returnedPageContainsAllFromExpectedPage() {
        int page = 0;
        int size = 1;

        Flashcard entity = getFlashcardWithId();
        Page<Flashcard> expectedEntityPage = new PageImpl<>(List.of(entity));
        Pageable pageable = PageRequest.of(page, size);

        when(flashcardRepository.findAllByCardFolderId(ID, pageable))
                .thenReturn(expectedEntityPage);

        Page<Flashcard> actualPage = flashcardService.getPageByFolderId(ID, page, size);

        assertThat(actualPage).containsAll(expectedEntityPage);
    }

    @Test
    void deleteFlashcardById_existsId_shouldInvokeRepository() {
        doNothing()
                .when(flashcardRepository).deleteById(ID);

        flashcardService.deleteFlashcardById(ID);

        verify(flashcardRepository).deleteById(ID);
    }

    @Test
    void createFlashcard_withCreateRequestAndData_shouldCreateFlashcard() {
        FlashcardCreateDtoRequest createDtoRequest = mock(FlashcardCreateDtoRequest.class);
        FlashcardCreationData creationData = mock(FlashcardCreationData.class);
        Flashcard expectedCreatedEntity = getFlashcardWithId();

        when(flashcardFactory.createFromRequestAndData(createDtoRequest, creationData))
                .thenReturn(expectedCreatedEntity);
        when(flashcardRepository.save(expectedCreatedEntity))
                .thenReturn(expectedCreatedEntity);

        Flashcard actualFlashcard = flashcardService.createFlashcard(createDtoRequest, creationData);

        assertThat(actualFlashcard).isEqualTo(expectedCreatedEntity);
    }

    @Test
    void updateFlashcard_existsId_shouldUpdate() {
        FlashcardUpdateDtoRequest updateDtoRequest = new FlashcardUpdateDtoRequest(ID, ID, "test-name", "test-descr", null);
        FlashcardUpdateData updateData = new FlashcardUpdateData("test-imageUrl");
        Flashcard entityToUpdate = getFlashcardWithId();

        when(flashcardRepository.findById(ID))
                .thenReturn(Optional.of(entityToUpdate));
        when(flashcardRepository.save(entityToUpdate))
                .thenReturn(entityToUpdate);

        Flashcard actualEntity = flashcardService.updateFlashcard(updateDtoRequest, updateData);

        assertThat(actualEntity).isEqualTo(entityToUpdate);
        assertThat(actualEntity.getName()).isEqualTo(updateDtoRequest.getName());
        assertThat(actualEntity.getDefinition()).isEqualTo(updateDtoRequest.getDefinition());
        assertThat(actualEntity.getImageUrl()).isEqualTo(updateData.imageUrl());
    }

    @Test
    void updateFlashcard_nonExistsId_throwsFlashcardNotFoundEx() {
        FlashcardUpdateDtoRequest updateDtoRequest = mock(FlashcardUpdateDtoRequest.class);
        FlashcardUpdateData updateData = mock(FlashcardUpdateData.class);

        when(flashcardRepository.findById(updateDtoRequest.getId()))
                .thenReturn(Optional.empty());

        assertThatThrownBy(() -> flashcardService.updateFlashcard(updateDtoRequest, updateData))
                .isInstanceOf(FlashcardNotFoundException.class);

        verify(flashcardRepository).findById(updateDtoRequest.getId());
    }

    private Flashcard getFlashcardWithId() {
        Flashcard flashcard = new Flashcard();
        flashcard.setId(ID);

        return flashcard;
    }
}
