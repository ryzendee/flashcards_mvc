package com.app.flashcards.factory;

import com.app.flashcards.dto.request.SignUpDtoRequest;
import com.app.flashcards.entity.User;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Component;

@Component
public class UserFactoryImpl implements UserFactory{

    @Override
    public User createFromSignUp(SignUpDtoRequest signUpDtoRequest, PasswordEncoder passwordEncoder) {
        return new User(signUpDtoRequest.getUsername(), passwordEncoder.encode(signUpDtoRequest.getPassword()));
    }
}
