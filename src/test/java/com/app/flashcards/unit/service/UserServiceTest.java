package com.app.flashcards.unit.service;

import com.app.flashcards.dto.auth.SignUpDto;
import com.app.flashcards.entity.User;
import com.app.flashcards.exception.custom.SignUpException;
import com.app.flashcards.factory.UserFactory;
import com.app.flashcards.repository.UserRepository;
import com.app.flashcards.service.user.UserServiceImpl;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.security.crypto.password.PasswordEncoder;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
public class UserServiceTest {

    private static final Long ID = 1L;
    @InjectMocks
    private UserServiceImpl userService;

    @Mock
    private UserRepository userRepository;
    @Mock
    private UserFactory userFactory;
    @Mock
    private PasswordEncoder passwordEncoder;

    @Test
    void createUser_withSignUpDto_createsUser() {
        SignUpDto signUpDto = getSignUpDto();
        User expectedUser = getUser();

        when(userFactory.createFromSignUp(signUpDto, passwordEncoder))
                .thenReturn(expectedUser);
        when(userRepository.save(expectedUser))
                .thenReturn(expectedUser);

        User actualUser = userService.createUser(signUpDto);
        assertThat(actualUser).isEqualTo(expectedUser);

        verify(userFactory).createFromSignUp(signUpDto, passwordEncoder);
        verify(userRepository).save(expectedUser);
    }

    @Test
    void createUser_repositoryThrowsDataIntegrityViolationEx_throwsSignUpEx() {
        SignUpDto signUpDto = getSignUpDto();
        User user = getUser();

        when(userFactory.createFromSignUp(signUpDto, passwordEncoder))
                .thenReturn(user);
        when(userRepository.save(user))
                .thenThrow(new DataIntegrityViolationException("test-msg"));

        assertThatThrownBy(() -> userService.createUser(signUpDto))
                .isInstanceOf(SignUpException.class);

        verify(userFactory).createFromSignUp(signUpDto, passwordEncoder);
        verify(userRepository).save(user);
    }

    private SignUpDto getSignUpDto() {
        return new SignUpDto("test-username", "test-pass", "test-pass");
    }

    private User getUser() {
        User user = new User();

        user.setId(ID);
        user.setUsername("test-username");
        user.setPassword("test-password");

        return user;
    }
}
