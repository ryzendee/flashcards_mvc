package com.app.flashcards.factory.cardfolder;

import com.app.flashcards.dto.request.CardFolderCreateDtoRequest;
import com.app.flashcards.entity.CardFolder;
import com.app.flashcards.entity.User;

public interface CardFolderFactory {

    CardFolder createFromRequest(CardFolderCreateDtoRequest createRequest, User user, String imageUrl);
}
