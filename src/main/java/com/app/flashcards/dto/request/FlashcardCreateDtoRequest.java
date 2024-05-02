package com.app.flashcards.dto.request;

import jakarta.validation.constraints.NotBlank;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import org.springframework.web.multipart.MultipartFile;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
public class FlashcardCreateDtoRequest {

    private Long folderId;
    @NotBlank(message = "Folder name must not be blank or empty.")
    private String name;
    private String definition;
    private MultipartFile image;
}