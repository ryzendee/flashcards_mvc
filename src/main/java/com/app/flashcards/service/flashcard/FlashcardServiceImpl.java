package com.app.flashcards.service.flashcard;

import com.app.flashcards.client.image.ImageCloudStorageClient;
import com.app.flashcards.dto.request.FlashcardCreateDtoRequest;
import com.app.flashcards.dto.request.FlashcardUpdateDtoRequest;
import com.app.flashcards.dto.response.FlashcardDtoResponse;
import com.app.flashcards.entity.CardFolder;
import com.app.flashcards.entity.Flashcard;
import com.app.flashcards.enums.ImagePath;
import com.app.flashcards.exception.cardfolder.CardFolderNotFoundException;
import com.app.flashcards.exception.flashcard.FlashcardCreateException;
import com.app.flashcards.exception.flashcard.FlashcardNotFoundException;
import com.app.flashcards.exception.flashcard.FlashcardUpdateException;
import com.app.flashcards.factory.flashcard.FlashcardFactory;
import com.app.flashcards.mapper.flashcard.FlashcardMapper;
import com.app.flashcards.models.ImageData;
import com.app.flashcards.repository.FlashcardRepository;
import com.app.flashcards.service.cardfolder.CardFolderService;
import jakarta.validation.ConstraintViolationException;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.multipart.MultipartFile;

import java.util.List;

@Service
@Slf4j
@RequiredArgsConstructor
public class FlashcardServiceImpl implements FlashcardService {

    private final FlashcardFactory flashcardFactory;
    private final FlashcardRepository flashcardRepository;
    private final FlashcardMapper flashcardMapper;
    private final CardFolderService cardFolderService;
    private final ImageCloudStorageClient imageCloudStorageClient;

    @Transactional(readOnly = true)
    @Override
    public List<FlashcardDtoResponse> getListByFolderId(Long folderId) {
        return flashcardRepository.findAllByCardFolderId(folderId).stream()
                .map(this::generateImageUrlAndMapToResponse).toList();
    }

    @Transactional(readOnly = true)
    @Override
    public Page<FlashcardDtoResponse> getPageByFolderId(Long folderId, int page, int size) {
        Pageable pageable = PageRequest.of(page, size);

        return flashcardRepository.findAllByCardFolderId(folderId, pageable)
                .map(this::generateImageUrlAndMapToResponse);
    }

    private FlashcardDtoResponse generateImageUrlAndMapToResponse(Flashcard entity) {
        String imageUrl = imageCloudStorageClient.generateUrlToImage(entity.getImagePath());
        FlashcardDtoResponse dtoResponse = flashcardMapper.toDto(entity);
        dtoResponse.setImageUrl(imageUrl);
        return dtoResponse;
    }

    @Transactional
    @Override
    public void deleteFlashcardById(Long flashcardId) {
        flashcardRepository.findById(flashcardId)
                .ifPresent(entity -> {
                    flashcardRepository.delete(entity);
                    imageCloudStorageClient.deleteImage(entity.getImagePath());
                    log.info("Deleted flashcard: {}", entity);
                });
    }

    @Transactional
    @Override
    public void createFlashcard(FlashcardCreateDtoRequest createDtoRequest) throws CardFolderNotFoundException {
        CardFolder cardFolder = cardFolderService.getById(createDtoRequest.getFolderId());

        Flashcard flashcard = flashcardFactory.createFromDtoRequest(createDtoRequest);
        flashcard.setCardFolder(cardFolder);

        String imagePath = uploadImage(createDtoRequest.getUserId(), createDtoRequest.getImage());
        flashcard.setImagePath(imagePath);

        try {
            flashcardRepository.save(flashcard);
        } catch (ConstraintViolationException ex) {
            log.error("Failed to create flashcard", ex);
            imageCloudStorageClient.deleteImage(imagePath);
            throw new FlashcardCreateException("Cannot update flashcard! Name, definition and card folder must not be null.");
        }
        log.info("Flashcard was saved: {}", flashcard);
    }

    @Transactional
    @Override
    public void updateFlashcard(FlashcardUpdateDtoRequest updateDtoRequest) {
        flashcardRepository.findById(updateDtoRequest.getId())
                .ifPresentOrElse(entity -> {
                    entity.setName(updateDtoRequest.getName());
                    entity.setDefinition(updateDtoRequest.getDefinition());

                    String imagePath = null;
                    if (!(updateDtoRequest.getImage() == null || updateDtoRequest.getImage().isEmpty())) {
                        imagePath = uploadImage(updateDtoRequest.getUserId(), updateDtoRequest.getImage());
                        entity.setImagePath(imagePath);
                    }

                    try {
                        flashcardRepository.saveAndFlush(entity);
                    } catch (ConstraintViolationException ex) {
                        log.error("Failed to update flashcard");

                        if (imagePath != null) {
                            imageCloudStorageClient.deleteImage(imagePath);
                        }

                        throw new FlashcardUpdateException("Cannot update flashcard! Name, definition and card folder must not be null.");
                    }

                    log.info("Flashcard was updated: {}", entity);
                }, () -> {
                    throw new FlashcardNotFoundException("Flashcard not found! Id: " + updateDtoRequest.getId());
                });
    }

    private String uploadImage(Long userId, MultipartFile image) {
        ImageData imageData = new ImageData(userId, image, ImagePath.FLASHCARDS_PATH);
        return imageCloudStorageClient.uploadImage(imageData);
    }
}
