package com.app.flashcards.mapper.cardfolder;

import com.app.flashcards.dto.response.CardFolderDtoResponse;
import com.app.flashcards.entity.CardFolder;

public interface CardFolderMapper {

    CardFolderDtoResponse toDto(CardFolder cardFolder);
}
