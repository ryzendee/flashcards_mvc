package com.app.flashcards.utils.path;

import com.app.flashcards.enums.ImagePath;
import com.app.flashcards.models.ImageData;
import org.springframework.stereotype.Component;

@Component
public class ImagePathGeneratorImpl implements ImagePathGenerator {

    //ex: user-{id}/{folder}/{filename}
    private static final String PATH_TEMPLATE = "user-%s/%s/%s";

    @Override
    public String generatePath(ImageData imageData) {
        String path = imageData.imagePath().getPathToImage();
        String imageFileName = imageData.image().getOriginalFilename();

        return PATH_TEMPLATE.formatted(String.valueOf(imageData.userId()), path, imageFileName);
    }
}
