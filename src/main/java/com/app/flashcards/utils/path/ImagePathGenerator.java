package com.app.flashcards.utils.path;

import com.app.flashcards.enums.ImagePath;

public interface ImagePathGenerator {

    String generatePath(String userId, String fileName, ImagePath path);
}
