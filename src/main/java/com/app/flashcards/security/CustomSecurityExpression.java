package com.app.flashcards.security;

import com.app.flashcards.service.cardfolder.CardFolderService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;

@Component
@Slf4j
@RequiredArgsConstructor
public class CustomSecurityExpression {

    private final CardFolderService cardFolderService;

    public boolean isFolderOwner(Long userId, Long folderId) {
        log.info("Checking that user is module owner: folderId={}, userId={}", userId, folderId);
        return cardFolderService.existsByIdAndUserId(folderId, userId);
    }
}
