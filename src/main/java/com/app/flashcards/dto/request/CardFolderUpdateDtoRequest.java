package com.app.flashcards.dto.request;

import jakarta.validation.constraints.NotBlank;
import lombok.*;
import org.springframework.web.multipart.MultipartFile;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
public class CardFolderUpdateDtoRequest {
    private Long id;
    private Long userId;
    @NotBlank(message = "Folder name must not be blank or empty.")
    private String name;
    private String description;
    private MultipartFile image;
}
