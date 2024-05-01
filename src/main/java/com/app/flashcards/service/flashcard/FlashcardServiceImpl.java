package com.app.flashcards.service.flashcard;

import com.app.flashcards.dto.request.FlashcardCreateDtoRequest;
import com.app.flashcards.dto.request.FlashcardUpdateDtoRequest;
import com.app.flashcards.entity.Flashcard;
import com.app.flashcards.exception.custom.FlashcardNotFoundException;
import com.app.flashcards.factory.flashcard.FlashcardFactory;
import com.app.flashcards.models.FlashcardCreationData;
import com.app.flashcards.models.FlashcardUpdateData;
import com.app.flashcards.repository.FlashcardRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@Slf4j
@RequiredArgsConstructor
public class FlashcardServiceImpl implements FlashcardService {

    private final FlashcardRepository flashcardRepository;
    private final FlashcardFactory flashcardFactory;

    @Override
    public List<Flashcard> getListByFolderId(Long folderId) {
        return flashcardRepository.findAllByCardFolderId(folderId);
    }

    @Override
    public Page<Flashcard> getPageByFolderId(Long folderId, int page, int size) {
        Pageable pageable = PageRequest.of(page, size);

        return flashcardRepository.findAllByCardFolderId(folderId, pageable);
    }

    @Override
    public void deleteFlashcardById(Long flashcardId) {
        log.info("Deleting flashcard with id: {}", flashcardId);
        flashcardRepository.deleteById(flashcardId);
    }

    @Override
    public Flashcard createFlashcard(FlashcardCreateDtoRequest createDtoRequest, FlashcardCreationData creationData) {
        Flashcard flashcard = flashcardFactory.createFromRequestAndData(createDtoRequest, creationData);
        flashcardRepository.save(flashcard);
        log.info("Flashcard was saved: {}", flashcard);

        return flashcard;
    }

    @Override
    public Flashcard updateFlashcard(FlashcardUpdateDtoRequest updateDtoRequest, FlashcardUpdateData updateData) {
        return flashcardRepository.findById(updateDtoRequest.getId())
                .map(entity -> {
                    entity.setName(updateDtoRequest.getName());
                    entity.setDefinition(updateDtoRequest.getDefinition());
                    entity.setImageUrl(updateData.imageUrl());
                    return flashcardRepository.save(entity);
                }).orElseThrow(() -> new FlashcardNotFoundException("Flashcard not found! Id: " + updateDtoRequest.getId()));
    }
}
