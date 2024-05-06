package com.app.flashcards.unit.utils;

import com.app.flashcards.enums.ImagePath;
import com.app.flashcards.utils.path.ImagePathGenerator;
import com.app.flashcards.utils.path.ImagePathGeneratorImpl;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import static org.assertj.core.api.Assertions.assertThat;

public class ImagePathGeneratorTest {

    private ImagePathGenerator imagePathGenerator;

    @BeforeEach
    void setUp() {
       imagePathGenerator = new ImagePathGeneratorImpl();
    }

    @Test
    void generatePath_withFlashcardsImagePath_shouldGenerateExpectedPath() {
        String expectedPath = "user-1/flashcards/test.png";

        String actualPath = imagePathGenerator.generatePath("1", "test.png", ImagePath.FLASHCARDS_PATH);

        assertThat(actualPath).isEqualTo(expectedPath);
    }
}
