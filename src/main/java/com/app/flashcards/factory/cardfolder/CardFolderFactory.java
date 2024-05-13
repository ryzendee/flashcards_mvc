package com.app.flashcards.factory.cardfolder;

import com.app.flashcards.dto.request.CardFolderCreateDtoRequest;
import com.app.flashcards.entity.CardFolder;

public interface CardFolderFactory {

    CardFolder createFromRequest(CardFolderCreateDtoRequest createRequest);
}
