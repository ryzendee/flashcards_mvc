package com.app.flashcards.service.cardfolder;

import com.app.flashcards.dto.request.CardFolderCreateDtoRequest;
import com.app.flashcards.dto.request.CardFolderUpdateDtoRequest;
import com.app.flashcards.entity.CardFolder;
import com.app.flashcards.exception.custom.CardFolderNotFoundException;
import com.app.flashcards.exception.custom.UserNotFoundException;
import com.app.flashcards.factory.cardfolder.CardFolderFactory;
import com.app.flashcards.models.CardFolderCreationData;
import com.app.flashcards.models.CardFolderUpdateData;
import com.app.flashcards.repository.CardFolderRepository;
import com.app.flashcards.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@Slf4j
@RequiredArgsConstructor
public class CardFolderServiceImpl implements CardFolderService {

    private final CardFolderRepository cardFolderRepository;
    private final UserRepository userRepository;
    private final CardFolderFactory cardFolderFactory;


    @Transactional
    @Override
    public CardFolder createCardFolder(CardFolderCreateDtoRequest createRequest, CardFolderCreationData data) {
        return userRepository.findById(data.userId())
                .map(user -> {
                    CardFolder cardFolder = cardFolderFactory.createFromRequest(createRequest, data);
                    cardFolder.setUser(user);
                    cardFolderRepository.save(cardFolder);
                    log.info("Card Folder was saved: {}", cardFolder);
                    return cardFolder;
                })
                .orElseThrow(() -> new UserNotFoundException("Cannot create cardFolder, because user not found! User id: " + data.userId()));
    }

    @Transactional(readOnly = true)
    @Override
    public Page<CardFolder> getCardFolderPageByUserId(Long userId, int page, int size) {
        Pageable pageable = PageRequest.of(page, size);

        return cardFolderRepository.findAllByUserId(userId, pageable);
    }

    @Transactional
    @Override
    public void deleteById(Long folderId) {
        log.info("Deleting folder with id: {}", folderId);
        cardFolderRepository.deleteById(folderId);
    }

    @Transactional
    @Override
    public CardFolder update(CardFolderUpdateDtoRequest request, CardFolderUpdateData data) {
        return cardFolderRepository.findById(request.getId())
                .map(entity -> {
                    entity.setName(request.getName());
                    entity.setDescription(request.getDescription());
                    entity.setImageUrl(data.imageUrl());
                    return cardFolderRepository.save(entity);
                }).orElseThrow(() -> new CardFolderNotFoundException("Card folder not found! Id: " + request.getId()));
    }

    @Transactional(readOnly = true)
    @Override
    public CardFolder getById(Long folderId) {
        return cardFolderRepository.findById(folderId)
                .orElseThrow(() -> new CardFolderNotFoundException("Card folder not found! Id: " + folderId));
    }

    @Transactional(readOnly = true)
    @Override
    public boolean existsByIdAndUserId(Long folderId, Long userId) {
        return cardFolderRepository.existsByIdAndUserId(folderId, userId);
    }
}
