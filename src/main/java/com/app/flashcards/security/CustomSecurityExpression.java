package com.app.flashcards.security;

import com.app.flashcards.service.cardfolder.CardFolderService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;
import org.springframework.web.bind.annotation.SessionAttribute;

@Component
@RequiredArgsConstructor
public class CustomSecurityExpression {

    private final CardFolderService cardFolderService;

    public boolean isFolderOwner(Long userId, Long folderId) {
        return cardFolderService.existsByUserId(userId, folderId);
    }
}
