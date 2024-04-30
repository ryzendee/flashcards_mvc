package com.app.flashcards.unit.facade;

import com.app.flashcards.dto.request.CardFolderCreateDtoRequest;
import com.app.flashcards.dto.request.CardFolderUpdateDtoRequest;
import com.app.flashcards.dto.response.CardFolderDtoResponse;
import com.app.flashcards.entity.CardFolder;
import com.app.flashcards.entity.User;
import com.app.flashcards.facade.cardfolder.CardFolderServiceFacadeImpl;
import com.app.flashcards.mapper.cardfolder.CardFolderMapper;
import com.app.flashcards.models.CardFolderCreationData;
import com.app.flashcards.models.CardFolderUpdateData;
import com.app.flashcards.models.ImageData;
import com.app.flashcards.service.cardfolder.CardFolderService;
import com.app.flashcards.service.image.ImageService;
import com.app.flashcards.service.user.UserService;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;

import java.util.ArrayList;
import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
public class CardFolderServiceFacadeTest {

    private static final Long ID = 1L;
    @InjectMocks
    private CardFolderServiceFacadeImpl cardFolderServiceFacade;

    @Mock
    private CardFolderService cardFolderService;
    @Mock
    private ImageService imageService;
    @Mock
    private UserService userService;
    @Mock
    private CardFolderMapper cardFolderMapper;


    @Test
    void createCardFolder_someState_success() {
        CardFolderCreateDtoRequest createRequest = new CardFolderCreateDtoRequest();
        String imageUrl = "test-url";
        User mockedUser = Mockito.mock(User.class);
        CardFolder folder = getCardFolderWithId();

        when(imageService.uploadImage(any(ImageData.class)))
                .thenReturn(imageUrl);
        when(userService.getUserById(ID))
                .thenReturn(mockedUser);
        when(cardFolderService.createCardFolder(eq(createRequest), any(CardFolderCreationData.class)))
                .thenReturn(folder);

        cardFolderServiceFacade.createCardFolder(ID, createRequest);

        verify(imageService).uploadImage(any(ImageData.class));
        verify(userService).getUserById(ID);
        verify(cardFolderService).createCardFolder(eq(createRequest), any(CardFolderCreationData.class));
    }

    @Test
    void getCardFoldersPageByUserId_state_returns() {
        int page = 0;
        int size = 1;
        CardFolderDtoResponse response = getDtoResponseWithId();

        CardFolder entity = getCardFolderWithId();
        Page<CardFolder> entityPage = new PageImpl<>(List.of(entity));

        when(cardFolderService.getCardFolderPageByUserId(ID, page, size))
                .thenReturn(entityPage);
        when(cardFolderMapper.toDto(entity))
                .thenReturn(response);

        Page<CardFolderDtoResponse> actualPage = cardFolderServiceFacade.getCardFolderPageByUserId(ID, page, size);
        assertThat(actualPage).contains(response);

        verify(cardFolderService).getCardFolderPageByUserId(ID, page, size);
        verify(cardFolderMapper).toDto(entity);
    }

    @Test
    void updateFolder_state_updates() {
        CardFolderUpdateDtoRequest updateDtoRequest = new CardFolderUpdateDtoRequest();
        String imageUrl = "test-url";
        CardFolder folder = getCardFolderWithId();

        when(imageService.uploadImage(any(ImageData.class)))
                .thenReturn(imageUrl);
        when(cardFolderService.update(eq(updateDtoRequest), any(CardFolderUpdateData.class)))
                .thenReturn(folder);

        cardFolderServiceFacade.updateCardFolder(ID, updateDtoRequest);

        verify(imageService).uploadImage(any(ImageData.class));
        verify(cardFolderService).update(eq(updateDtoRequest), any(CardFolderUpdateData.class));
    }

    @Test
    void deleteById_with_shouldDelete() {
        doNothing()
                .when(cardFolderService).deleteById(ID);

        cardFolderServiceFacade.deleteById(ID);

        verify(cardFolderService).deleteById(ID);
    }

    private CardFolder getCardFolderWithId() {
        CardFolder folder = new CardFolder();
        folder.setId(ID);

        return folder;
    }

    private CardFolderDtoResponse getDtoResponseWithId() {
        CardFolderDtoResponse response = new CardFolderDtoResponse();
        response.setId(ID);

        return response;
    }
}
