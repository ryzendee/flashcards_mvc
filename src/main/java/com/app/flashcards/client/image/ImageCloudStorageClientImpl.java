package com.app.flashcards.service.image;

import com.app.flashcards.enums.ImagePath;
import com.app.flashcards.models.ImageData;
import com.app.flashcards.utils.path.ImagePathGenerator;
import io.micrometer.common.util.StringUtils;
import io.minio.*;
import io.minio.errors.*;
import io.minio.http.Method;
import jakarta.validation.constraints.NotBlank;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.security.InvalidKeyException;
import java.security.NoSuchAlgorithmException;
import java.util.concurrent.TimeUnit;

@Service
@Slf4j
public class ImageCloudStorageClientImpl implements ImageCloudStorageClient {
    private static final int IMAGE_EXPIRATION_TIME = 10;
    private static final String DEFAULT_PATH = ImagePath.DEFAULT.getPathToImage();

    private final MinioClient minioClient;
    private final ImagePathGenerator imagePathGenerator;
    private final String bucketName;

    public ImageCloudStorageClientImpl(MinioClient minioClient,
                                       ImagePathGenerator imagePathGenerator,
                                       @Value("${minio.bucket-name}") @NotBlank String bucketName) {
        this.minioClient = minioClient;
        this.imagePathGenerator = imagePathGenerator;
        this.bucketName = bucketName;
    }

    @Override
    public String uploadImage(ImageData imageData) {
        MultipartFile image = imageData.image();

        if (image == null || image.isEmpty()) {
            return DEFAULT_PATH;
        }

        try {
            String generatedPath = imagePathGenerator.generatePath(imageData);

            minioClient.putObject(
                    PutObjectArgs
                            .builder()
                            .bucket(this.bucketName)
                            .object(generatedPath)
                            .stream(image.getInputStream(), image.getSize(), -1)
                            .contentType(image.getContentType())
                            .build()
            );

            log.info("Image was uploaded, path: {}", generatedPath);

            return generatedPath;
        } catch (IOException | MinioException | NoSuchAlgorithmException | InvalidKeyException ex) {
            log.error("Image upload exception", ex);
            return DEFAULT_PATH;
        }
    }

    @Override
    public String generateUrlToImage(String path) {
        if (!isImageExists(path) || path.equals(DEFAULT_PATH)) {
            return DEFAULT_PATH;
        }

        try {
            String imageUrl = minioClient.getPresignedObjectUrl(
                    GetPresignedObjectUrlArgs.builder()
                            .method(Method.GET)
                            .bucket(bucketName)
                            .object(path)
                            .expiry(IMAGE_EXPIRATION_TIME, TimeUnit.HOURS)
                            .build());

            log.info("Generate URL to image: {}", imageUrl);
            return imageUrl;
        } catch (ServerException | InsufficientDataException | ErrorResponseException |
                 IOException | NoSuchAlgorithmException | InvalidKeyException |
                 InvalidResponseException | XmlParserException | InternalException ex) {
            log.error("Failed to generate url to file", ex);
            return DEFAULT_PATH;
        }
    }

    @Override
    public boolean deleteImage(String path) {
        if (!isImageExists(path) || path.equals(DEFAULT_PATH)) {
            return false;
        }

        try {
            minioClient.removeObject(
                    RemoveObjectArgs.builder()
                            .bucket(bucketName)
                            .object(path)
                            .build()
            );
            log.info("Image was deleted: {}", path);
            return true;
        } catch (MinioException | IOException | NoSuchAlgorithmException | InvalidKeyException ex) {
            log.error("Image delete exception", ex);
            return false;
        }
    }

    private boolean isImageExists(String imagePath) {
        if (StringUtils.isBlank(imagePath)) {
            return false;
        }

        try {
            minioClient.statObject(
                    StatObjectArgs.builder()
                            .bucket(bucketName)
                            .object(imagePath)
                            .build()
            );

            return true;
        } catch (ErrorResponseException | InsufficientDataException | InternalException | InvalidKeyException |
                 InvalidResponseException | IOException | NoSuchAlgorithmException | ServerException |
                 XmlParserException ex) {
            return false;
        }
    }
}
