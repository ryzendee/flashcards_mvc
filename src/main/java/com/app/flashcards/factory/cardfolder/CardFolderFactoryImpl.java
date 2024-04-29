package com.app.flashcards.factory.cardfolder;

import com.app.flashcards.dto.request.CardFolderCreateDtoRequest;
import com.app.flashcards.entity.CardFolder;
import com.app.flashcards.models.CardFolderCreationData;
import org.springframework.stereotype.Component;

@Component
public class CardFolderFactoryImpl implements CardFolderFactory {

    @Override
    public CardFolder createFromRequest(CardFolderCreateDtoRequest createRequest, CardFolderCreationData data) {
        return new CardFolder(createRequest.getName(), createRequest.getDescription(), data.imageUrl(), data.user());
    }
}
