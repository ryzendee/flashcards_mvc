package com.app.flashcards.facade.cardfolder;

import com.app.flashcards.dto.request.CardFolderCreateDtoRequest;
import com.app.flashcards.dto.request.CardFolderUpdateDtoRequest;
import com.app.flashcards.dto.response.CardFolderDtoResponse;
import com.app.flashcards.entity.CardFolder;
import com.app.flashcards.entity.User;
import com.app.flashcards.enums.ImagePath;
import com.app.flashcards.mapper.cardfolder.CardFolderMapper;
import com.app.flashcards.models.CardFolderCreationData;
import com.app.flashcards.models.CardFolderUpdateData;
import com.app.flashcards.models.ImageData;
import com.app.flashcards.service.cardfolder.CardFolderService;
import com.app.flashcards.service.image.ImageService;
import com.app.flashcards.service.user.UserService;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

@Service
@RequiredArgsConstructor
public class CardFolderServiceFacadeImpl implements CardFolderServiceFacade {
    private final CardFolderService cardFolderService;
    private final ImageService imageService;
    private final UserService userService;
    private final CardFolderMapper cardFolderMapper;

    @Override
    public void createCardFolder(Long userId, CardFolderCreateDtoRequest createRequest) {

        ImageData imageData = createImageData(userId, createRequest.getImage());
        String imageUrl = imageService.uploadImage(imageData);
        User user = userService.getUserById(userId);
        CardFolderCreationData data = new CardFolderCreationData(user, imageUrl);

        CardFolder cardFolder = cardFolderService.createCardFolder(createRequest, data);
    }

    @Override
    public Page<CardFolderDtoResponse> getCardFolderPageByUserId(Long userId, int page, int size) {
        return cardFolderService.getCardFolderPageByUserId(userId, page, size)
                .map(cardFolderMapper::toDto);
    }

    @Override
    public void updateCardFolder(Long userId, CardFolderUpdateDtoRequest updateRequest) {
        ImageData imageData = createImageData(userId, updateRequest.getImage());
        String imageUrl = imageService.uploadImage(imageData);
        CardFolderUpdateData data = new CardFolderUpdateData(imageUrl);
        CardFolder updatedFolder = cardFolderService.update(updateRequest, data);
    }

    @Override
    public void deleteById(Long folderId) {
        cardFolderService.deleteById(folderId);
    }

    @Override
    public CardFolderUpdateDtoRequest mapToUpdateRequest(CardFolderDtoResponse response) {
        return cardFolderMapper.toUpdateRequest(response);
    }

    private ImageData createImageData(Long userId, MultipartFile image) {
        return new ImageData(userId, image, ImagePath.CARDFOLDER_PATH);
    }
}
