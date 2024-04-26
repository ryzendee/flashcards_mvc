package com.app.flashcards.facade.cardfolder;

import com.app.flashcards.dto.request.CardFolderCreateDtoRequest;
import com.app.flashcards.dto.response.CardFolderDtoResponse;
import com.app.flashcards.entity.CardFolder;
import com.app.flashcards.entity.User;
import com.app.flashcards.enums.ImagePath;
import com.app.flashcards.mapper.cardfolder.CardFolderMapper;
import com.app.flashcards.service.cardfolder.CardFolderService;
import com.app.flashcards.service.image.ImageService;
import com.app.flashcards.service.user.UserService;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class CardFolderServiceFacadeImpl implements CardFolderServiceFacade {
    private final CardFolderService cardFolderService;
    private final ImageService imageService;
    private final UserService userService;
    private final CardFolderMapper cardFolderMapper;

    @Override
    public void createCardFolder(Long userId, CardFolderCreateDtoRequest createRequest) {
        User user = userService.getUserById(userId);
        String imageUrl = imageService.uploadImage(userId, createRequest.getImage(), ImagePath.CARDFOLDER_PATH);
        CardFolder cardFolder = cardFolderService.createCardFolder(createRequest, user, imageUrl);
    }

    @Override
    public Page<CardFolderDtoResponse> getCardFolderPageByUserId(Long userId, int page, int size) {
        return cardFolderService.getCardFolderPageByUserId(userId, page, size)
                .map(cardFolderMapper::toDto);
    }

}
