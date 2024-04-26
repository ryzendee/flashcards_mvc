package com.app.flashcards.service.image;

import com.app.flashcards.enums.ImagePath;
import org.springframework.web.multipart.MultipartFile;

public interface ImageService {
    String uploadImage(Long userId, MultipartFile image, ImagePath imagePath);
    void deleteImage(String bucketName, String path);

}