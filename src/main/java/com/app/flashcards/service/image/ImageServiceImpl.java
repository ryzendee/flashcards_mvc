package com.app.flashcards.service.image;

import com.app.flashcards.enums.ImagePath;
import com.app.flashcards.exception.custom.ImageUploadException;
import com.app.flashcards.models.ImageData;
import com.app.flashcards.utils.cloud.CloudStorageClient;
import com.app.flashcards.utils.path.ImagePathGenerator;
import io.minio.errors.MinioException;
import jakarta.validation.constraints.NotBlank;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.security.InvalidKeyException;
import java.security.NoSuchAlgorithmException;

@Service
@Slf4j
public class ImageServiceImpl implements ImageService {

    private final CloudStorageClient minioUtils;
    private final ImagePathGenerator imagePathGenerator;
    private final String endpoint;
    private final String bucketName;

    public ImageServiceImpl(CloudStorageClient minioUtils,
                            ImagePathGenerator imagePathGenerator,
                            @Value("${minio.endpoint}") @NotBlank String endpoint,
                            @Value("${minio.bucket-name}") @NotBlank String bucketName) {
        this.minioUtils = minioUtils;
        this.imagePathGenerator = imagePathGenerator;
        this.endpoint = endpoint;
        this.bucketName = bucketName;
    }

    @Override
    public String uploadImage(ImageData imageData) {
        MultipartFile image = imageData.image();

        if (image.isEmpty()) {
            return ImagePath.DEFAULT.getPathToImage();
        }

        try {
            String generatedPath = imagePathGenerator.generatePath(String.valueOf(imageData.userId()), image.getOriginalFilename(), imageData.imagePath());
            minioUtils.uploadFile(bucketName, generatedPath, image);

            String url =  minioUtils.getUrlToFile(bucketName, generatedPath);
            log.info("Generated URL to file: {}", url);

            return url;
        } catch (IOException | MinioException | NoSuchAlgorithmException | InvalidKeyException ex) {
            log.error("Image upload exception", ex);
            throw new ImageUploadException("Failed to upload image");
        }
    }

    @Override
    public void deleteImage(String bucketName, String path) {
        try {
            minioUtils.deleteFile(bucketName, path);
        } catch (IOException | MinioException | NoSuchAlgorithmException | InvalidKeyException ex) {
            log.error("Image delete exception", ex);
            throw new ImageUploadException("Failed to delete image");

        }
    }
}
