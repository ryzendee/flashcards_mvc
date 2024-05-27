package com.app.flashcards.dto.request;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;
import lombok.*;
import org.springframework.web.multipart.MultipartFile;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
public class CardFolderUpdateDtoRequest {
    private Long id;

    private Long userId;

    @NotBlank(message = "Name must not be blank or empty")
    @Size(max = 50, message = "Name must be equal or less than 50 symbols")
    private String name;

    @Size(max = 255, message = "Description must be equal or less than 255 symbols")
    private String description;

    private MultipartFile image;
}
