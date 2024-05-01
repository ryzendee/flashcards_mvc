package com.app.flashcards.service.flashcard;

import com.app.flashcards.dto.request.FlashcardCreateDtoRequest;
import com.app.flashcards.dto.request.FlashcardUpdateDtoRequest;
import com.app.flashcards.entity.Flashcard;
import com.app.flashcards.models.FlashcardCreationData;
import com.app.flashcards.models.FlashcardUpdateData;
import org.springframework.data.domain.Page;

import java.util.List;

public interface FlashcardService {

    List<Flashcard> getListByFolderId(Long folderId);
    Page<Flashcard> getPageByFolderId(Long folderId, int page, int size);
    void deleteFlashcardById(Long flashcardId);
    Flashcard createFlashcard(FlashcardCreateDtoRequest createDtoRequest, FlashcardCreationData creationData);
    Flashcard updateFlashcard(FlashcardUpdateDtoRequest updateDtoRequest, FlashcardUpdateData updateData);
}
