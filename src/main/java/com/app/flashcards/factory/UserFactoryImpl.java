package com.app.flashcards.factory;

import com.app.flashcards.dto.auth.SignUpDto;
import com.app.flashcards.entity.User;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Component;

@Component
public class UserFactoryImpl implements UserFactory{

    @Override
    public User createFromSignUp(SignUpDto signUpDto, PasswordEncoder passwordEncoder) {
        return new User(signUpDto.getUsername(), passwordEncoder.encode(signUpDto.getPassword()));
    }
}
