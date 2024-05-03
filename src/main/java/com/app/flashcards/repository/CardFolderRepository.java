package com.app.flashcards.repository;

import com.app.flashcards.entity.CardFolder;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;

public interface CardFolderRepository extends JpaRepository<CardFolder, Long> {

    Page<CardFolder> findAllByUserId(Long userId, Pageable pageable);

    boolean existsByIdAndUserId(Long id, Long userId);
}
