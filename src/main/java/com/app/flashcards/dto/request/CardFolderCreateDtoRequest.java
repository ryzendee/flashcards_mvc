package com.app.flashcards.dto.request;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import org.springframework.web.multipart.MultipartFile;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
public class CardFolderCreateDtoRequest {
    private Long userId;

    @NotBlank(message = "Folder name must not be blank or empty.")
    @Size(max = 50, message = "Name must be equal or less than 50 symbols")
    private String name;

    @Size(max = 255, message = "Description must be equal or less than 255 symbols")
    private String description;

    private MultipartFile image;
}
