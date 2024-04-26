package com.app.flashcards.dto.response;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
@AllArgsConstructor
public class CardFolderDtoResponse {

    private Long id;
    private String name;
    private String description;
    private String imageUrl;
}
