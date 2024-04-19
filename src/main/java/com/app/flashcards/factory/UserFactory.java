package com.app.flashcards.factory;

import com.app.flashcards.dto.request.SignUpDtoRequest;
import com.app.flashcards.entity.User;
import org.springframework.security.crypto.password.PasswordEncoder;

public interface UserFactory {

    User createFromSignUp(SignUpDtoRequest signUpDtoRequest, PasswordEncoder passwordEncoder);
}
