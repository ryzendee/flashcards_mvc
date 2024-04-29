package com.app.flashcards.facade.cardfolder;

import com.app.flashcards.dto.request.CardFolderCreateDtoRequest;
import com.app.flashcards.dto.request.CardFolderUpdateDtoRequest;
import com.app.flashcards.dto.response.CardFolderDtoResponse;
import org.springframework.data.domain.Page;

public interface CardFolderServiceFacade {

    void createCardFolder(Long userId, CardFolderCreateDtoRequest createRequest);
    Page<CardFolderDtoResponse> getCardFolderPageByUserId(Long userId, int page, int size);
    void updateCardFolder(Long userId, CardFolderUpdateDtoRequest updateRequest);
    void deleteById(Long folderId);
    CardFolderUpdateDtoRequest mapToUpdateRequest(CardFolderDtoResponse response);
}
