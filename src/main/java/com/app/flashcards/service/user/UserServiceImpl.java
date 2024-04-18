package com.app.flashcards.service.user;

import com.app.flashcards.dto.auth.SignUpDto;
import com.app.flashcards.entity.User;
import com.app.flashcards.exception.custom.SignUpException;
import com.app.flashcards.factory.UserFactory;
import com.app.flashcards.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;


@Service
@Slf4j
@RequiredArgsConstructor
public class UserServiceImpl implements UserService {

    private final UserRepository userRepository;
    private final UserFactory userFactory;
    private final PasswordEncoder passwordEncoder;

    @Override
    public User createUser(SignUpDto signUpDto) {
        User user = userFactory.createFromSignUp(signUpDto, passwordEncoder);

        try {
            userRepository.save(user);
            log.info("User was saved: {}", user);

            return user;
        } catch (DataIntegrityViolationException ex) {
            log.error("Failed to save user", ex);
            throw new SignUpException("User with this username already exists.");
        }
    }

    @Override
    public User updateUser(User user) {
        return null;
    }

    @Override
    public void deleteUserById(Long id) {
    }
}