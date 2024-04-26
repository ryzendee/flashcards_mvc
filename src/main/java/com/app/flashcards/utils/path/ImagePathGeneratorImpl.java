package com.app.flashcards.utils.path;

import com.app.flashcards.enums.ImagePath;
import org.springframework.stereotype.Component;

@Component
public class ImagePathGeneratorImpl implements ImagePathGenerator {

    //ex: user-{id}/{folder}/{filename}
    private static final String PATH_TEMPLATE = "user-%s/%s/%s";

    @Override
    public String generatePath(String userId, String fileName, ImagePath path) {
        return PATH_TEMPLATE.formatted(userId, path.getPathToImage(), fileName);
    }
}
