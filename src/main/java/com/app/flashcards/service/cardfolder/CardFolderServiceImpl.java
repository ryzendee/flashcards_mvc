package com.app.flashcards.service.cardfolder;

import com.app.flashcards.dto.request.CardFolderCreateDtoRequest;
import com.app.flashcards.entity.CardFolder;
import com.app.flashcards.entity.User;
import com.app.flashcards.factory.cardfolder.CardFolderFactory;
import com.app.flashcards.repository.CardFolderRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

@Service
@Slf4j
@RequiredArgsConstructor
public class CardFolderServiceImpl implements CardFolderService {

    private final CardFolderRepository cardFolderRepository;
    private final CardFolderFactory cardFolderFactory;


    @Override
    public CardFolder createCardFolder(CardFolderCreateDtoRequest createRequest, User user, String imageUrl) {
        CardFolder cardFolder = cardFolderFactory.createFromRequest(createRequest, user, imageUrl);
        cardFolderRepository.save(cardFolder);

        log.info("Card Folder was saved: {}", cardFolder);

        return cardFolder;
    }

    @Override
    public Page<CardFolder> getCardFolderPageByUserId(Long userId, int page, int size) {
        Pageable pageable = PageRequest.of(page, size);

        return cardFolderRepository.findAllByUserId(userId, pageable);
    }
}
