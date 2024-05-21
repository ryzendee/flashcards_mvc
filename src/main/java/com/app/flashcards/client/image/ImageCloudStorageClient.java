package com.app.flashcards.client.image;

import com.app.flashcards.models.ImageData;

public interface ImageCloudStorageClient {
    String uploadImage(ImageData imageData);
    boolean deleteImage(String imagePath);
    String generateUrlToImage(String imagePath);
}