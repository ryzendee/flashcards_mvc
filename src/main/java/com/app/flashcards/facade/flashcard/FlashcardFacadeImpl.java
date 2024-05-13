package com.app.flashcards.facade.flashcard;

import com.app.flashcards.dto.request.FlashcardCreateDtoRequest;
import com.app.flashcards.dto.request.FlashcardUpdateDtoRequest;
import com.app.flashcards.dto.response.FlashcardDtoResponse;
import com.app.flashcards.entity.CardFolder;
import com.app.flashcards.enums.ImagePath;
import com.app.flashcards.models.FlashcardCreationData;
import com.app.flashcards.models.FlashcardUpdateData;
import com.app.flashcards.models.ImageData;
import com.app.flashcards.service.cardfolder.CardFolderService;
import com.app.flashcards.service.flashcard.FlashcardService;
import com.app.flashcards.mapper.flashcard.FlashcardMapper;
import com.app.flashcards.service.image.ImageService;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.stereotype.Component;
import org.springframework.web.multipart.MultipartFile;

import java.util.List;

@Component
@RequiredArgsConstructor
public class FlashcardFacadeImpl implements FlashcardFacade {

    private final ImageService imageService;
    private final CardFolderService cardFolderService;
    private final FlashcardMapper flashcardMapper;
    private final FlashcardService flashcardService;

    @Override
    public List<FlashcardDtoResponse> getListByFolderId(Long folderId) {
        return flashcardService.getListByFolderId(folderId).stream()
                .map(flashcardMapper::toDto)
                .toList();
    }

    @Override
    public Page<FlashcardDtoResponse> getPageByFolderId(Long folderId, int page, int size) {
        return flashcardService.getPageByFolderId(folderId, page, size)
                .map(flashcardMapper::toDto);
    }

    @Override
    public void createFlashcard(Long userId, FlashcardCreateDtoRequest createDtoRequest) {
        ImageData imageData = createImageData(userId, createDtoRequest.getImage());
        String imageUrl = imageService.uploadImage(imageData);
        FlashcardCreationData creationData = new FlashcardCreationData(imageUrl);
        flashcardService.createFlashcard(createDtoRequest, creationData);
    }

    @Override
    public void updateFlashcard(Long userId, FlashcardUpdateDtoRequest updateDtoRequest) {
        ImageData imageData = createImageData(userId, updateDtoRequest.getImage());
        String imageUrl = imageService.uploadImage(imageData);
        FlashcardUpdateData updateData = new FlashcardUpdateData(imageUrl);
        flashcardService.updateFlashcard(updateDtoRequest, updateData);
    }

    @Override
    public void deleteById(Long flashcardId) {
        flashcardService.deleteFlashcardById(flashcardId);
    }

    private ImageData createImageData(Long userId, MultipartFile image) {
        return new ImageData(userId, image, ImagePath.FLASHCARDS_PATH);
    }
}
