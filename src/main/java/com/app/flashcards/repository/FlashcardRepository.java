package com.app.flashcards.repository;

import com.app.flashcards.entity.Flashcard;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface FlashcardRepository extends JpaRepository<Flashcard, Long> {
    List<Flashcard> findAllByCardFolderId(Long folderId);
    Page<Flashcard> findAllByCardFolderId(Long folderId, Pageable pageable);
}
