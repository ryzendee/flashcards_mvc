package com.app.flashcards.service.cardfolder;

import com.app.flashcards.dto.request.CardFolderCreateDtoRequest;
import com.app.flashcards.dto.request.CardFolderUpdateDtoRequest;
import com.app.flashcards.entity.CardFolder;
import com.app.flashcards.models.CardFolderCreationData;
import com.app.flashcards.models.CardFolderUpdateData;
import org.springframework.data.domain.Page;

public interface CardFolderService {
    CardFolder createCardFolder(CardFolderCreateDtoRequest createRequest, CardFolderCreationData creationData);

    Page<CardFolder> getCardFolderPageByUserId(Long userId, int page, int size);
    void deleteById(Long folderId);
    CardFolder update(CardFolderUpdateDtoRequest request, CardFolderUpdateData data);
    CardFolder getById(Long folderId);
    boolean existsByUserId(Long folderId, Long userId);
}
