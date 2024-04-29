package com.app.flashcards.models;

import com.app.flashcards.enums.ImagePath;
import lombok.Getter;
import lombok.RequiredArgsConstructor;
import org.springframework.web.multipart.MultipartFile;

public record ImageData(Long userId, MultipartFile image, ImagePath imagePath) {
}
