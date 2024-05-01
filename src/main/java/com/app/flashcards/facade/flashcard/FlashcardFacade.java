package com.app.flashcards.facade.flashcard;

import com.app.flashcards.dto.request.FlashcardCreateDtoRequest;
import com.app.flashcards.dto.request.FlashcardUpdateDtoRequest;
import com.app.flashcards.dto.response.FlashcardDtoResponse;
import org.springframework.data.domain.Page;

import java.util.List;

public interface FlashcardFacade {
    List<FlashcardDtoResponse> getListByFolderId(Long folderId);

    Page<FlashcardDtoResponse> getPageByFolderId(Long folderId, int page, int size);

    void createFlashcard(Long userId, FlashcardCreateDtoRequest createDtoRequest);
    void updateFlashcard(Long userId, FlashcardUpdateDtoRequest updateDtoRequest);

    void deleteById(Long flashcardId);
}
