package com.app.flashcards.service.flashcard;

import com.app.flashcards.dto.request.FlashcardCreateDtoRequest;
import com.app.flashcards.dto.request.FlashcardUpdateDtoRequest;
import com.app.flashcards.dto.response.FlashcardDtoResponse;
import org.springframework.data.domain.Page;

import java.util.List;

public interface FlashcardService {

    List<FlashcardDtoResponse> getListByFolderId(Long folderId);
    Page<FlashcardDtoResponse> getPageByFolderId(Long folderId, int page, int size);
    void deleteFlashcardById(Long flashcardId);
    void createFlashcard(FlashcardCreateDtoRequest createDtoRequest);
    void updateFlashcard(FlashcardUpdateDtoRequest updateDtoRequest);
}
