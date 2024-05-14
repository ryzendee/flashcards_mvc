package com.app.flashcards.unit.utils;

import com.app.flashcards.enums.ImagePath;
import com.app.flashcards.models.ImageData;
import com.app.flashcards.utils.path.ImagePathGenerator;
import com.app.flashcards.utils.path.ImagePathGeneratorImpl;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.Mockito;
import org.springframework.web.multipart.MultipartFile;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

class ImagePathGeneratorTest {

    private ImagePathGenerator imagePathGenerator;

    @BeforeEach
    void setUp() {
       imagePathGenerator = new ImagePathGeneratorImpl();
    }

    @Test
    void generatePath_withFlashcardsImagePath_shouldGenerateExpectedPath() {
        String expectedPath = "user-1/flashcards/test.png";

        MultipartFile image = Mockito.mock(MultipartFile.class);
        ImageData imageData = new ImageData(1L, image, ImagePath.FLASHCARDS_PATH);
        when(image.getOriginalFilename())
                .thenReturn("test.png");

        String actualPath = imagePathGenerator.generatePath(imageData);
        assertThat(actualPath).isEqualTo(expectedPath);

        verify(image).getOriginalFilename();
    }
}
