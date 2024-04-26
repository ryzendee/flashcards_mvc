package com.app.flashcards.factory.cardfolder;

import com.app.flashcards.dto.request.CardFolderCreateDtoRequest;
import com.app.flashcards.entity.CardFolder;
import com.app.flashcards.entity.User;
import org.springframework.stereotype.Component;

@Component
public class CardFolderFactoryImpl implements CardFolderFactory {

    @Override
    public CardFolder createFromRequest(CardFolderCreateDtoRequest createRequest, User user, String imageUrl) {
        return new CardFolder(createRequest.getName(), createRequest.getDescription(), imageUrl, user);
    }
}
