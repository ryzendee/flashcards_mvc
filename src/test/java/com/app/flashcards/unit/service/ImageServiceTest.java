package com.app.flashcards.unit.service;

import com.app.flashcards.enums.ImagePath;
import com.app.flashcards.exception.custom.ImageUploadException;
import com.app.flashcards.models.ImageData;
import com.app.flashcards.service.image.ImageServiceImpl;
import com.app.flashcards.utils.cloud.CloudStorageClient;
import com.app.flashcards.utils.path.ImagePathGenerator;
import io.minio.errors.MinioException;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.security.InvalidKeyException;
import java.security.NoSuchAlgorithmException;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
public class ImageServiceTest {
    private static final Long ID = 1L;
    private static final ImagePath IMAGE_PATH = ImagePath.DEFAULT;
    @InjectMocks
    private ImageServiceImpl imageService;

    @Mock
    private CloudStorageClient cloudStorageClient;
    @Mock
    private ImagePathGenerator imagePathGenerator;

    @Test
    void uploadImage_emptyImage_returnsDefaultPath() {
        ImageData imageData = getImageDataWithMockedFile();
        MultipartFile image = imageData.image();

        when(image.isEmpty())
                .thenReturn(true);

        String urlToImage = imageService.uploadImage(imageData);
        assertThat(urlToImage).isEqualTo(ImagePath.DEFAULT.getPathToImage());

        verify(image).isEmpty();
    }

    @Test
    void uploadImage_imageNotEmpty_returnsUrlToImage() throws Exception {
        ImageData imageData = getImageDataWithMockedFile();
        MultipartFile image = imageData.image();
        String path = "test-path";
        String expectedUrl = "test-url";

        when(image.isEmpty())
                .thenReturn(false);
        when(imagePathGenerator.generatePath(String.valueOf(imageData.userId()), image.getOriginalFilename(), imageData.imagePath()))
                .thenReturn(path);
        when(cloudStorageClient.getUrlToFile(any(String.class), eq(path)))
                .thenReturn(expectedUrl);

        String actualUrl = imageService.uploadImage(imageData);
        assertThat(actualUrl).isEqualTo(expectedUrl);

        verify(image).isEmpty();
        verify(imagePathGenerator).generatePath(String.valueOf(imageData.userId()), image.getOriginalFilename(), imageData.imagePath());
        verify(cloudStorageClient).getUrlToFile(any(String.class), eq(path));
    }

    //Upload with exceptions
    @Test
    void uploadImage_clientThrowsIOEx_shouldThrowImageUploadEx() throws Exception {
        ImageData imageData = getImageDataWithMockedFile();

        doThrow(new IOException("test-msg"))
                .when(cloudStorageClient).uploadFile(any(String.class), any(String.class), eq(imageData.image()));

        assertThatThrownBy(() -> imageService.uploadImage(imageData))
                .isInstanceOf(ImageUploadException.class);

        verify(cloudStorageClient).uploadFile(any(String.class), any(String.class) ,eq(imageData.image()));
    }
    @Test
    void uploadImage_clientThrowsMinioEx_shouldThrowImageUploadEx() throws Exception {
        ImageData imageData = getImageDataWithMockedFile();

        doThrow(new MinioException("test-msg"))
                .when(cloudStorageClient).uploadFile(any(String.class), any(String.class), eq(imageData.image()));

        assertThatThrownBy(() -> imageService.uploadImage(imageData))
                .isInstanceOf(ImageUploadException.class);

        verify(cloudStorageClient).uploadFile(any(String.class), any(String.class) ,eq(imageData.image()));

    }
    @Test
    void uploadImage_clientThrowsNoSuchAlgorithmEx_shouldThrowImageUploadEx() throws Exception {
        ImageData imageData = getImageDataWithMockedFile();

        doThrow(new NoSuchAlgorithmException("test-msg"))
                .when(cloudStorageClient).uploadFile(any(String.class), any(String.class), eq(imageData.image()));

        assertThatThrownBy(() -> imageService.uploadImage(imageData))
                .isInstanceOf(ImageUploadException.class);

        verify(cloudStorageClient).uploadFile(any(String.class), any(String.class) ,eq(imageData.image()));

    }
    @Test
    void uploadImage_clientThrowsInvalidKeyEx_shouldThrowImageUploadEx() throws Exception {
        ImageData imageData = getImageDataWithMockedFile();

        doThrow(new InvalidKeyException("test-msg"))
                .when(cloudStorageClient).uploadFile(any(String.class), any(String.class), eq(imageData.image()));

        assertThatThrownBy(() -> imageService.uploadImage(imageData))
                .isInstanceOf(ImageUploadException.class);

        verify(cloudStorageClient).uploadFile(any(String.class), any(String.class) ,eq(imageData.image()));

    }

    private ImageData getImageDataWithMockedFile() {
        MultipartFile image = mock(MultipartFile.class);
        return new ImageData(1L, image, ImagePath.DEFAULT);
    }
}
