package com.app.flashcards.factory.cardfolder;

import com.app.flashcards.dto.request.CardFolderCreateDtoRequest;
import com.app.flashcards.entity.CardFolder;
import com.app.flashcards.entity.User;
import com.app.flashcards.models.CardFolderCreationData;

public interface CardFolderFactory {

    CardFolder createFromRequest(CardFolderCreateDtoRequest createRequest, CardFolderCreationData data);
}
