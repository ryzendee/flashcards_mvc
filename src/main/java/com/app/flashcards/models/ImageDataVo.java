package com.app.flashcards.models;

import com.app.flashcards.enums.ImagePath;
import org.springframework.web.multipart.MultipartFile;

public record ImageDataVo(Long userId, MultipartFile image, ImagePath imagePath) {
}
