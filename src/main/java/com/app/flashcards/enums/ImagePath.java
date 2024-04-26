package com.app.flashcards.enums;

import lombok.Getter;
import lombok.RequiredArgsConstructor;

@Getter
@RequiredArgsConstructor
public enum ImagePath {


    DEFAULT("/default_image.png"),

    //Without slash, because we have a pattern to format in PathGenerator
    CARDFOLDER_PATH("card-folders"),
    FLASHCARDS_PATH("flashcards");

    private final String pathToImage;


}
