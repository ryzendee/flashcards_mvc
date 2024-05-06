package com.app.flashcards.unit.facade;

import com.app.flashcards.dto.request.FlashcardCreateDtoRequest;
import com.app.flashcards.dto.request.FlashcardUpdateDtoRequest;
import com.app.flashcards.dto.response.FlashcardDtoResponse;
import com.app.flashcards.entity.CardFolder;
import com.app.flashcards.entity.Flashcard;
import com.app.flashcards.facade.flashcard.FlashcardFacadeImpl;
import com.app.flashcards.mapper.flashcard.FlashcardMapper;
import com.app.flashcards.models.FlashcardCreationData;
import com.app.flashcards.models.FlashcardUpdateData;
import com.app.flashcards.models.ImageData;
import com.app.flashcards.service.cardfolder.CardFolderService;
import com.app.flashcards.service.flashcard.FlashcardService;
import com.app.flashcards.service.image.ImageService;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;

import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
public class FlashcardServiceFacadeTest {

    private static final Long ID = 1L;
    private static final String IMAGE_URL = "test-url";
    @InjectMocks
    private FlashcardFacadeImpl flashcardFacade;

    @Mock
    private ImageService imageService;
    @Mock
    private CardFolderService cardFolderService;
    @Mock
    private FlashcardMapper flashcardMapper;
    @Mock
    private FlashcardService flashcardService;

    @Test
    void getListByFolderId() {
        FlashcardDtoResponse dtoResponse = getDtoResponseWithId();
        Flashcard entity = getFlashcardWithId();
        List<Flashcard> entityList = List.of(entity);

        when(flashcardService.getListByFolderId(ID))
                .thenReturn(entityList);
        when(flashcardMapper.toDto(entity))
                .thenReturn(dtoResponse);

        List<FlashcardDtoResponse> responseList = flashcardFacade.getListByFolderId(ID);

        assertThat(responseList).hasSize(entityList.size());
        assertThat(responseList).contains(dtoResponse);

        verify(flashcardService).getListByFolderId(ID);
        verify(flashcardMapper).toDto(entity);
    }

    @Test
    void getPageByFolderId() {
        int page = 0;
        int size = 1;

        FlashcardDtoResponse dtoResponse = getDtoResponseWithId();
        Flashcard entity = getFlashcardWithId();
        Page<Flashcard> entityPage = new PageImpl<>(List.of(entity));

        when(flashcardService.getPageByFolderId(ID, page, size))
                .thenReturn(entityPage);
        when(flashcardMapper.toDto(entity))
                .thenReturn(dtoResponse);

        Page<FlashcardDtoResponse> actualPage = flashcardFacade.getPageByFolderId(ID, page, size);

        assertThat(actualPage).hasSize(entityPage.getSize());
        assertThat(actualPage).contains(dtoResponse);

        verify(flashcardService).getPageByFolderId(ID, page, size);
        verify(flashcardMapper).toDto(entity);
    }

    @Test
    void createFlashcard() {
        FlashcardCreateDtoRequest createDtoRequest = mock(FlashcardCreateDtoRequest.class);
        CardFolder cardFolder = mock(CardFolder.class);
        Flashcard createdEntity = getFlashcardWithId();

        when(imageService.uploadImage(any(ImageData.class)))
                .thenReturn(IMAGE_URL);
        when(cardFolderService.getById(createDtoRequest.getFolderId()))
                .thenReturn(cardFolder);
        when(flashcardService.createFlashcard(eq(createDtoRequest), any(FlashcardCreationData.class)))
                .thenReturn(createdEntity);

        flashcardFacade.createFlashcard(ID, createDtoRequest);

        verify(imageService).uploadImage(any(ImageData.class));
        verify(cardFolderService).getById(createDtoRequest.getFolderId());
        verify(flashcardService).createFlashcard(eq(createDtoRequest), any(FlashcardCreationData.class));
    }

    @Test
    void updateFlashcard() {
        FlashcardUpdateDtoRequest updateDtoRequest = mock(FlashcardUpdateDtoRequest.class);
        Flashcard updatedEntity = getFlashcardWithId();

        when(imageService.uploadImage(any(ImageData.class)))
                .thenReturn(IMAGE_URL);
        when(flashcardService.updateFlashcard(eq(updateDtoRequest), any(FlashcardUpdateData.class)))
                .thenReturn(updatedEntity);

        flashcardFacade.updateFlashcard(ID, updateDtoRequest);

        verify(imageService).uploadImage(any(ImageData.class));
        verify(flashcardService).updateFlashcard(eq(updateDtoRequest), any(FlashcardUpdateData.class));
    }

    @Test
    void deleteFlashcard() {
        doNothing()
                .when(flashcardService).deleteFlashcardById(ID);

        flashcardFacade.deleteById(ID);

        verify(flashcardService).deleteFlashcardById(ID);
    }

    private Flashcard getFlashcardWithId() {
        Flashcard flashcard = new Flashcard();
        flashcard.setId(ID);

        return new Flashcard();
    }

    private FlashcardDtoResponse getDtoResponseWithId() {
        FlashcardDtoResponse dtoResponse = new FlashcardDtoResponse();
        dtoResponse.setId(ID);

        return dtoResponse;
    }
}
