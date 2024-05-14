package com.app.flashcards.mapper.cardfolder;

import com.app.flashcards.dto.response.CardFolderDtoResponse;
import com.app.flashcards.entity.CardFolder;
import org.springframework.stereotype.Component;

@Component
public class CardFolderMapperImpl implements CardFolderMapper {

    @Override
    public CardFolderDtoResponse toDto(CardFolder cardFolder) {
        return new CardFolderDtoResponse(
                cardFolder.getId(),
                cardFolder.getName(),
                cardFolder.getDescription(),
                cardFolder.getImagePath());
    }
}
