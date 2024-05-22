package com.app.flashcards.client.image;

import com.app.flashcards.models.ImageDataVo;

public interface ImageCloudStorageClient {
    String uploadImage(ImageDataVo imageDataVo);
    boolean deleteImage(String imagePath);
    String generateUrlToImage(String imagePath);
}