package com.app.flashcards.dto.response;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
public class FlashcardDtoResponse {

    private Long id;
    private Long folderId;
    private String name;
    private String definition;
    private String imageUrl;
}
