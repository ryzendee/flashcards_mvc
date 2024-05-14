package com.app.flashcards.service.cardfolder;

import com.app.flashcards.dto.request.CardFolderCreateDtoRequest;
import com.app.flashcards.dto.request.CardFolderUpdateDtoRequest;
import com.app.flashcards.dto.response.CardFolderDtoResponse;
import com.app.flashcards.entity.CardFolder;
import com.app.flashcards.enums.ImagePath;
import com.app.flashcards.exception.custom.UserNotFoundException;
import com.app.flashcards.factory.cardfolder.CardFolderFactory;
import com.app.flashcards.mapper.cardfolder.CardFolderMapper;
import com.app.flashcards.models.ImageData;
import com.app.flashcards.repository.CardFolderRepository;
import com.app.flashcards.repository.UserRepository;
import com.app.flashcards.service.image.ImageCloudStorageClient;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.multipart.MultipartFile;

@Service
@Slf4j
@RequiredArgsConstructor
public class CardFolderServiceImpl implements CardFolderService {

    private final CardFolderRepository cardFolderRepository;
    private final UserRepository userRepository;
    private final CardFolderFactory cardFolderFactory;
    private final CardFolderMapper cardFolderMapper;
    private final ImageCloudStorageClient imageCloudStorageClient;

    @Transactional
    @Override
    public void createCardFolder(CardFolderCreateDtoRequest createRequest) {
        userRepository.findById(createRequest.getUserId())
                .ifPresentOrElse(user -> {
                    ImageData imageData = buildImageData(createRequest.getUserId(), createRequest.getImage());
                    String imagePath = imageCloudStorageClient.uploadImage(imageData);

                    CardFolder cardFolder = cardFolderFactory.createFromRequest(createRequest);
                    cardFolder.setUser(user);
                    cardFolder.setImagePath(imagePath);

                    cardFolderRepository.save(cardFolder);
                    log.info("Card Folder was saved: {}", cardFolder);
                }, () -> {
                    throw new UserNotFoundException("Cannot create cardFolder, because user not found! User id: " + createRequest.getUserId());
                });
    }


    @Transactional(readOnly = true)
    @Override
    public Page<CardFolderDtoResponse> getCardFolderPageByUserId(Long userId, int page, int size) {
        Pageable pageable = PageRequest.of(page, size);

        return cardFolderRepository.findAllByUserId(userId, pageable)
                .map(entity -> {
                    CardFolderDtoResponse cardFolderDtoResponse = cardFolderMapper.toDto(entity);
                    String imageUrl = imageCloudStorageClient.generateUrlToImage(entity.getImagePath());
                    cardFolderDtoResponse.setImageUrl(imageUrl);
                    return cardFolderDtoResponse;
                });
    }

    @Transactional
    @Override
    public void deleteById(Long folderId) {
        cardFolderRepository.findById(folderId)
                .ifPresent(entity -> {
                    cardFolderRepository.delete(entity);
                    imageCloudStorageClient.deleteImage(entity.getImagePath());
                    log.info("Deleted card folder: {}", entity);
                });
    }

    @Transactional
    @Override
    public void updateCardFolder(CardFolderUpdateDtoRequest request) {
        cardFolderRepository.findById(request.getId())
                .ifPresent(entity -> {
                    ImageData imageData = buildImageData(request.getUserId(), request.getImage());
                    String imagePath = imageCloudStorageClient.uploadImage(imageData);

                    entity.setImagePath(imagePath);
                    entity.setName(request.getName());
                    entity.setDescription(request.getDescription());

                    cardFolderRepository.save(entity);
                    log.info("Card folder was updated: {}", entity);
                });
    }

    @Transactional(readOnly = true)
    @Override
    public boolean existsByIdAndUserId(Long folderId, Long userId) {
        return cardFolderRepository.existsByIdAndUserId(folderId, userId);
    }

    private ImageData buildImageData(Long userId, MultipartFile image) {
        return new ImageData(userId, image, ImagePath.CARDFOLDER_PATH);
    }

}
