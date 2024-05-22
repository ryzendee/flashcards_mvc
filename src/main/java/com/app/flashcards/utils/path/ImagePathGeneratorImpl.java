package com.app.flashcards.utils.path;

import com.app.flashcards.models.ImageDataVo;
import org.springframework.stereotype.Component;

@Component
public class ImagePathGeneratorImpl implements ImagePathGenerator {

    //ex: user-{id}/{folder}/{filename}
    private static final String PATH_TEMPLATE = "user-%s/%s/%s";

    @Override
    public String generatePath(ImageDataVo imageDataVo) {
        String path = imageDataVo.imagePath().getPathToImage();
        String imageFileName = imageDataVo.image().getOriginalFilename();

        return PATH_TEMPLATE.formatted(String.valueOf(imageDataVo.userId()), path, imageFileName);
    }
}
