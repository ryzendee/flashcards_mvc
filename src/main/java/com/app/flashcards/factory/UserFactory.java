package com.app.flashcards.factory;

import com.app.flashcards.dto.auth.SignUpDto;
import com.app.flashcards.entity.User;
import org.springframework.security.crypto.password.PasswordEncoder;

public interface UserFactory {

    User createFromSignUp(SignUpDto signUpDto, PasswordEncoder passwordEncoder);
}
