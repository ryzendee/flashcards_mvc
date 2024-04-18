package com.app.flashcards.dto.auth;

import com.app.flashcards.annotations.validation.PasswordMatcher;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
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
public class SignUpDto {

    @NotBlank(message = "Username must not be blank or empty.")
    private String username;

    @NotBlank(message = "Password must not be blank or empty.")
    @Size(min = 5, message = "Password must contain at least 5 symbols")
    private String password;

    private String passwordConfirmation;

}
