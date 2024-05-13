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

    private final CloudStorageClient cloudStorageClient;
    private final ImagePathGenerator imagePathGenerator;
    private final String bucketName;

    public ImageServiceImpl(CloudStorageClient cloudStorageClient,
                            ImagePathGenerator imagePathGenerator,
                            @Value("${minio.bucket-name}") @NotBlank String bucketName) {
        this.cloudStorageClient = cloudStorageClient;
        this.imagePathGenerator = imagePathGenerator;
        this.bucketName = bucketName;
    }

    @Override
    public String uploadImage(ImageData imageData) {
        MultipartFile image = imageData.image();

        if (image.isEmpty()) {
            return ImagePath.DEFAULT.getPathToImage();
        }

        try {
            String generatedPath = imagePathGenerator.generatePath(imageData);
            cloudStorageClient.uploadFile(this.bucketName, generatedPath, image);

            String url =  cloudStorageClient.getUrlToFile(this.bucketName, generatedPath);
            log.info("Generated URL to file: {}", url);

            return url;
        } catch (IOException | MinioException | NoSuchAlgorithmException | InvalidKeyException ex) {
            log.error("Image upload exception", ex);
            throw new ImageUploadException("Failed to upload image");
        }
    }

    @Override
    public boolean deleteImage(String path) {
        try {
            cloudStorageClient.deleteFile(this.bucketName, path);
            return true;
        } catch (MinioException | IOException | NoSuchAlgorithmException | InvalidKeyException ex) {
            log.error("Image delete exception", ex);
            return false;
        }
    }
}
