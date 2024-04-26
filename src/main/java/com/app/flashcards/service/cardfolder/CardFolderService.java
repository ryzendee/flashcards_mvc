package com.app.flashcards.service.cardfolder;

import com.app.flashcards.dto.request.CardFolderCreateDtoRequest;
import com.app.flashcards.entity.CardFolder;
import com.app.flashcards.entity.User;
import org.springframework.data.domain.Page;

public interface CardFolderService {
    CardFolder createCardFolder(CardFolderCreateDtoRequest createRequest, User user, String imageUrl);

    Page<CardFolder> getCardFolderPageByUserId(Long userId, int page, int size);
}
