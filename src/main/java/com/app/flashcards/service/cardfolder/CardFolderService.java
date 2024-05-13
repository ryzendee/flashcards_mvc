package com.app.flashcards.service.cardfolder;

import com.app.flashcards.dto.request.CardFolderCreateDtoRequest;
import com.app.flashcards.dto.request.CardFolderUpdateDtoRequest;
import com.app.flashcards.dto.response.CardFolderDtoResponse;
import org.springframework.data.domain.Page;

public interface CardFolderService {
    void createCardFolder(Long userId, CardFolderCreateDtoRequest createRequest);

    Page<CardFolderDtoResponse> getCardFolderPageByUserId(Long userId, int page, int size);
    void deleteById(Long folderId);
    void updateCardFolder(CardFolderUpdateDtoRequest request);
    boolean existsByIdAndUserId(Long folderId, Long userId);
}
