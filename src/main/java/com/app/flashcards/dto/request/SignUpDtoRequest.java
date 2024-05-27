package com.app.flashcards.dto.request;

import com.app.flashcards.annotations.validation.PasswordMatcher;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@PasswordMatcher
@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
public class SignUpDtoRequest {

    @NotBlank(message = "Username must not be blank or empty.")
    @Size(max = 50, message = "Username must be equal or less than 50 symbols")
    private String username;

    @NotBlank(message = "Password must not be blank or empty.")
    @Size(min = 5, max = 50, message = "Password must contain at least 5 symbols and equal or less than 50 symbols")
    private String password;

    private String passwordConfirmation;

}
