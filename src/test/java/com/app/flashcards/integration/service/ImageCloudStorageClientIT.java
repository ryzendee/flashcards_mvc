package com.app.flashcards.integration.service;

import com.app.flashcards.integration.ITBase;
import com.app.flashcards.enums.ImagePath;
import com.app.flashcards.models.ImageDataVo;
import com.app.flashcards.client.image.ImageCloudStorageClient;
import com.app.flashcards.utils.path.ImagePathGenerator;
import io.minio.*;
import io.minio.errors.ErrorResponseException;
import io.minio.messages.Item;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.mock.web.MockMultipartFile;
import org.springframework.web.multipart.MultipartFile;

import java.util.ArrayList;
import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;

class ImageCloudStorageClientIT extends ITBase {

    private static final ImagePath FLASHCARD_IMAGE_PATH = ImagePath.FLASHCARDS_PATH;
    private static final ImagePath DEFAULT_IMAGE_PATH = ImagePath.DEFAULT;
    private static final Long USER_ID = 1L;

    @Value("${minio.bucket-name}")
    private String bucketName;
    @Autowired
    private ImageCloudStorageClient imageCloudStorageClient;

    @Autowired
    private MinioClient minioClient;

    @Autowired
    private ImagePathGenerator imagePathGenerator;


    @BeforeEach
    void setUp() throws Exception {
        //Getting and deleting all files in bucket
        Iterable<Result<Item>> minioImages = minioClient.listObjects(
                ListObjectsArgs.builder()
                        .bucket(bucketName)
                        .recursive(true)
                        .build()
        );

        for (Result<Item> item : minioImages) {
            String objectName = item.get().objectName();
            minioClient.removeObject(
                    RemoveObjectArgs.builder()
                            .bucket(bucketName)
                            .object(objectName)
                            .build()
            );
        }
    }

    @Test
    void uploadImage_imageDataContainsImage_shouldUploadAndReturnImagePath() {
        //given
        ImageDataVo imageDataVo = getImageDataWithImage();

        //when
        String path = imageCloudStorageClient.uploadImage(imageDataVo);

        //then
        boolean isImageExists;

        try {
            minioClient.statObject(
                    StatObjectArgs.builder()
                            .bucket(bucketName)
                            .object(path)
                            .build()
            );
            isImageExists = true;
        } catch (ErrorResponseException ex) {
            isImageExists = false;
        } catch (Exception ex) {
            throw new RuntimeException(ex);
        }

        assertThat(isImageExists).isTrue();
    }

    @Test
    void uploadImage_imageInImageDataIsEmpty_shouldReturnDefaultImagePath() {
        //given
        MockMultipartFile emptyImage = new MockMultipartFile("test", (byte[]) null);
        ImageDataVo imageDataVo = new ImageDataVo(USER_ID, emptyImage, FLASHCARD_IMAGE_PATH);

        //when
        String path = imageCloudStorageClient.uploadImage(imageDataVo);

        //then
        assertThat(path).isEqualTo(DEFAULT_IMAGE_PATH.getPathToImage());

        Iterable<Result<Item>> resultIterable = minioClient.listObjects(
                ListObjectsArgs.builder()
                        .bucket(bucketName)
                        .build()
        );

        boolean isBucketEmpty = !resultIterable.iterator().hasNext();
        assertThat(isBucketEmpty).isTrue();
    }

    @Test
    void uploadImage_imageInImageDataIsNull_shouldReturnDefaultImagePath() {
        //given
        ImageDataVo imageDataVo = new ImageDataVo(USER_ID, null, FLASHCARD_IMAGE_PATH);

        //when
        String path = imageCloudStorageClient.uploadImage(imageDataVo);

        //then
        assertThat(path).isEqualTo(DEFAULT_IMAGE_PATH.getPathToImage());

        List<Item> itemList = new ArrayList<>();
        minioClient.listObjects(
                ListObjectsArgs.builder()
                        .bucket(bucketName)
                        .build()
        ).forEach(
                result -> {
                    try {
                        itemList.add(result.get());
                    } catch (Exception ex) {
                        throw new RuntimeException(ex);
                    }
                });
        assertThat(itemList).isEmpty();
    }

    @Test
    void generateUrlToImage_pathInArgsEqualsDefaultImagePath_shouldReturnPathFromArgs() {
        String generatedUrl = imageCloudStorageClient.generateUrlToImage(DEFAULT_IMAGE_PATH.getPathToImage());
        assertThat(generatedUrl).isEqualTo(DEFAULT_IMAGE_PATH.getPathToImage());
    }

    @Test
    void generateUrlToImage_existsImageAndValidPath_shouldReturnUrlToImage() {
        //given
        ImageDataVo imageDataVo = getImageDataWithImage();
        String imagePath = imagePathGenerator.generatePath(imageDataVo);

        MultipartFile image = imageDataVo.image();
        try {
            minioClient.putObject(
                    PutObjectArgs
                            .builder()
                            .bucket(bucketName)
                            .object(imagePath)
                            .stream(image.getInputStream(), image.getSize(), -1)
                            .contentType(image.getContentType())
                            .build()
            );
        } catch (Exception ex) {
            throw new RuntimeException(ex);
        }

        //when
        String generatedUrl = imageCloudStorageClient.generateUrlToImage(imagePath);

        //then
        assertThat(generatedUrl).contains(imagePath);
    }

    @Test
    void generateUrlToImage_nonExistsImage_shouldReturnDefaultPath() {
        //given
        String imagePath = "path-to-generate-url";

        //when
        String generatedUrl = imageCloudStorageClient.generateUrlToImage(imagePath);

        //then
        assertThat(generatedUrl).isEqualTo(DEFAULT_IMAGE_PATH.getPathToImage());
    }

    @Test
    void deleteImage_existsImage_shouldDeleteAndReturnTrue() {
        //given
        ImageDataVo imageDataVo = getImageDataWithImage();
        String imagePath = imagePathGenerator.generatePath(imageDataVo);

        MultipartFile image = imageDataVo.image();
        try {
            minioClient.putObject(
                    PutObjectArgs
                            .builder()
                            .bucket(bucketName)
                            .object(imagePath)
                            .stream(image.getInputStream(), image.getSize(), -1)
                            .contentType(image.getContentType())
                            .build()
            );
        } catch (Exception ex) {
            throw new RuntimeException(ex);
        }

        //when
        boolean result = imageCloudStorageClient.deleteImage(imagePath);

        //then
        assertThat(result).isTrue();
    }

    @Test
    void deleteImage_nonExistsImage_shouldReturnFalse() {
        //given
        String nonExistsImagePath = "test-path";

        //when
        boolean result = imageCloudStorageClient.deleteImage(nonExistsImagePath);

        //then
        assertThat(result).isFalse();
    }

    private ImageDataVo getImageDataWithImage() {
        MockMultipartFile image = new MockMultipartFile(
                "file",
                "test.jpg",
                "image/jpg",
                "test".getBytes()
        );
        return new ImageDataVo(USER_ID, image, FLASHCARD_IMAGE_PATH);
    }
}
