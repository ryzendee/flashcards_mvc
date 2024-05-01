package com.app.flashcards.dto.response;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor //Added
public class CardFolderDtoResponse {

    private Long id;
    private String name;
    private String description;
    private String imageUrl;
}
