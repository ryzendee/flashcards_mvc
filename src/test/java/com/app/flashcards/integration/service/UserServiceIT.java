package com.app.flashcards.integration.service;

import com.app.flashcards.dto.request.SignUpDtoRequest;
import com.app.flashcards.entity.User;
import com.app.flashcards.exception.user.SignUpException;
import com.app.flashcards.exception.user.UserNotFoundException;
import com.app.flashcards.integration.ITBase;
import com.app.flashcards.repository.UserRepository;
import com.app.flashcards.service.user.UserService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.NullSource;
import org.springframework.beans.factory.annotation.Autowired;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;

class UserServiceIT extends ITBase {

    private static final String USERNAME = "test-username";
    private static final String PASSWORD = "test-password";

    @Autowired
    private UserRepository userRepository;
    @Autowired
    private UserService userService;

    @BeforeEach
    void setUp() {
        userRepository.deleteAll();
    }


    @Test
    void createUser_validDtoRequest_shouldCreate() {
        //given
        SignUpDtoRequest signUpDtoRequest = new SignUpDtoRequest(USERNAME, PASSWORD, PASSWORD);

        //when
        userService.createUser(signUpDtoRequest);

        //then
        User createdUser = userRepository.findAll().stream()
                .findFirst()
                .orElse(null);

        assertThat(createdUser).isNotNull();
        assertThat(createdUser.getUsername()).isEqualTo(signUpDtoRequest.getUsername());
        assertThat(createdUser.getPassword()).isNotBlank();
    }

    @Test
    void createUser_existsUsername_throwsEx() {
        //given
        User user = saveAndGetUser();

        SignUpDtoRequest signUpDtoRequest = new SignUpDtoRequest(user.getUsername(), user.getPassword(), user.getPassword());

        //when
        //then
        assertThatThrownBy(() -> userService.createUser(signUpDtoRequest))
                .isInstanceOf(SignUpException.class);
    }

    @NullSource
    @ParameterizedTest
    void createUser_invalidDto_throwsSignUpEx(String invalidUsername) {
        SignUpDtoRequest signUpDtoRequest = new SignUpDtoRequest(invalidUsername, PASSWORD, PASSWORD);

        assertThatThrownBy(() -> userService.createUser(signUpDtoRequest))
                .isInstanceOf(SignUpException.class);
    }

    @Test
    void getUserById_existsUser_returnsUser() {
        //given
        User expectedUser = saveAndGetUser();

        //when
        User actualUser = userService.getUserById(expectedUser.getId());

        //then
        assertThat(actualUser.getUsername()).isEqualTo(expectedUser.getUsername());
        assertThat(actualUser.getPassword()).isEqualTo(expectedUser.getPassword());
    }

    @Test
    void getUserById_nonExistsUser_throwUserNotFoundEx() {
        Long id = 1L;

        assertThatThrownBy(() -> userService.getUserById(id))
                .isInstanceOf(UserNotFoundException.class);
    }

    private User saveAndGetUser() {
        User user = new User();
        user.setUsername(USERNAME);
        user.setPassword(PASSWORD);
        return userRepository.save(user);
    }
}
