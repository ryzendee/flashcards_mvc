package com.app.flashcards.unit.service;

import com.app.flashcards.dto.request.CardFolderCreateDtoRequest;
import com.app.flashcards.dto.request.CardFolderUpdateDtoRequest;
import com.app.flashcards.entity.CardFolder;
import com.app.flashcards.exception.custom.CardFolderNotFoundException;
import com.app.flashcards.factory.cardfolder.CardFolderFactory;
import com.app.flashcards.models.CardFolderCreationData;
import com.app.flashcards.models.CardFolderUpdateData;
import com.app.flashcards.repository.CardFolderRepository;
import com.app.flashcards.service.cardfolder.CardFolderServiceImpl;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;

import java.util.List;
import java.util.Optional;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.eq;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
public class CardFolderServiceTest {

    private static final Long ID = 1L;
    @InjectMocks
    private CardFolderServiceImpl cardFolderService;

    @Mock
    private CardFolderRepository cardFolderRepository;
    @Mock
    private CardFolderFactory cardFolderFactory;

    @Test
    void createCardFolder_withRequestAndData_shouldCreate() {
        CardFolderCreateDtoRequest createDtoRequest = Mockito.mock(CardFolderCreateDtoRequest.class);
        CardFolderCreationData creationData = Mockito.mock(CardFolderCreationData.class);
        CardFolder expectedFolder = getCardFolderWithId();

        when(cardFolderFactory.createFromRequest(createDtoRequest, creationData))
                .thenReturn(expectedFolder);
        when(cardFolderRepository.save(expectedFolder))
                .thenReturn(expectedFolder);

        CardFolder actualFolder = cardFolderService.createCardFolder(createDtoRequest, creationData);
        assertThat(actualFolder).isEqualTo(expectedFolder);

        verify(cardFolderFactory).createFromRequest(createDtoRequest, creationData);
        verify(cardFolderRepository).save(expectedFolder);
    }

    @Test
    void getCardFolderPageByUserId_withPageAndSize_returnsPage() {
        int page = 0;
        int size = 1;

        CardFolder cardFolder = getCardFolderWithId();
        Page<CardFolder> entityPage = new PageImpl<>(List.of(cardFolder));
        Pageable pageable = PageRequest.of(page, size);


        when(cardFolderRepository.findAllByUserId(ID, pageable))
                .thenReturn(entityPage);

        Page<CardFolder> actualPage = cardFolderService.getCardFolderPageByUserId(ID, page, size);
        assertThat(actualPage).containsAll(entityPage);

        verify(cardFolderRepository).findAllByUserId(ID, pageable);
    }

    @Test
    void deleteById_withId_shouldInvokeRepository() {
        doNothing()
                .when(cardFolderRepository).deleteById(ID);

        cardFolderService.deleteById(ID);

        verify(cardFolderRepository).deleteById(ID);
    }

    @Test
    void update_existsCardFolder_shouldUpdate() {
        CardFolderUpdateDtoRequest request = new CardFolderUpdateDtoRequest(ID, "test-name", "test-descr", null);
        CardFolderUpdateData updateData = new CardFolderUpdateData("test-imageUrl");

        CardFolder entityToUpdate = getCardFolderWithId();

        when(cardFolderRepository.findById(ID))
                .thenReturn(Optional.of(entityToUpdate));
        when(cardFolderRepository.save(entityToUpdate))
                .thenReturn(entityToUpdate);

        CardFolder actualEntity = cardFolderService.update(request, updateData);

        assertThat(actualEntity).isEqualTo(entityToUpdate);
        assertThat(actualEntity.getName()).isEqualTo(request.getName());
        assertThat(actualEntity.getDescription()).isEqualTo(request.getDescription());
        assertThat(actualEntity.getImageUrl()).isEqualTo(updateData.imageUrl());

        verify(cardFolderRepository).findById(ID);
        verify(cardFolderRepository).save(entityToUpdate);
    }

    @Test
    void updateById_nonExistsId_throwCardFolderNotFoundEx() {
        CardFolderUpdateDtoRequest request = new CardFolderUpdateDtoRequest();
        request.setId(ID);
        CardFolderUpdateData data = Mockito.mock(CardFolderUpdateData.class);

        when(cardFolderRepository.findById(ID))
                .thenReturn(Optional.empty());

        assertThatThrownBy(() -> cardFolderService.update(request, data))
                .isInstanceOf(CardFolderNotFoundException.class);

        verify(cardFolderRepository).findById(ID);
    }

    @Test
    void getById_existsId_returnsCardFolder() {
        CardFolder expectedCardFolder = getCardFolderWithId();

        when(cardFolderRepository.findById(ID))
                .thenReturn(Optional.of(expectedCardFolder));

        CardFolder actualCardFolder = cardFolderService.getById(ID);
        assertThat(actualCardFolder).isEqualTo(expectedCardFolder);

        verify(cardFolderRepository).findById(ID);
    }

    @Test
    void getById_nonExistsId_throwCardFolderNotFoundEx() {
        when(cardFolderRepository.findById(ID))
                .thenReturn(Optional.empty());

        assertThatThrownBy(() -> cardFolderService.getById(ID))
                .isInstanceOf(CardFolderNotFoundException.class);

        verify(cardFolderRepository).findById(ID);
    }

    @Test
    void existsByIdAndUserId_existsBothIds_returnsTrue() {
        when(cardFolderRepository.existsByIdAndUserId(ID, ID))
                .thenReturn(true);

        boolean result = cardFolderService.existsByIdAndUserId(ID, ID);
        assertThat(result).isTrue();

        verify(cardFolderRepository).existsByIdAndUserId(ID, ID);
    }

    private CardFolder getCardFolderWithId() {
        CardFolder cardFolder = new CardFolder();
        cardFolder.setId(ID);

        return cardFolder;
    }
}
