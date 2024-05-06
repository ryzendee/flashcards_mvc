package com.app.flashcards.unit.service;

import com.app.flashcards.dto.request.SignUpDtoRequest;
import com.app.flashcards.entity.User;
import com.app.flashcards.exception.custom.SignUpException;
import com.app.flashcards.exception.custom.UserNotFoundException;
import com.app.flashcards.factory.user.UserFactory;
import com.app.flashcards.repository.UserRepository;
import com.app.flashcards.service.user.UserServiceImpl;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.security.crypto.password.PasswordEncoder;

import java.util.Optional;

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
        SignUpDtoRequest signUpDtoRequest = getSignUpDto();
        User expectedUser = getUser();

        when(userFactory.createFromSignUp(signUpDtoRequest, passwordEncoder))
                .thenReturn(expectedUser);
        when(userRepository.save(expectedUser))
                .thenReturn(expectedUser);

        User actualUser = userService.createUser(signUpDtoRequest);
        assertThat(actualUser).isEqualTo(expectedUser);

        verify(userFactory).createFromSignUp(signUpDtoRequest, passwordEncoder);
        verify(userRepository).save(expectedUser);
    }

    @Test
    void createUser_repositoryThrowsDataIntegrityViolationEx_throwsSignUpEx() {
        SignUpDtoRequest signUpDtoRequest = getSignUpDto();
        User user = getUser();

        when(userFactory.createFromSignUp(signUpDtoRequest, passwordEncoder))
                .thenReturn(user);
        when(userRepository.save(user))
                .thenThrow(new DataIntegrityViolationException("test-msg"));

        assertThatThrownBy(() -> userService.createUser(signUpDtoRequest))
                .isInstanceOf(SignUpException.class);

        verify(userFactory).createFromSignUp(signUpDtoRequest, passwordEncoder);
        verify(userRepository).save(user);
    }

    @Test
    void getUserById_existsId_returnsUser() {
        User expectedUser = getUser();

        when(userRepository.findById(ID))
                .thenReturn(Optional.of(expectedUser));

        User actualUser = userService.getUserById(ID);
        assertThat(actualUser).isEqualTo(expectedUser);

        verify(userRepository).findById(ID);
    }

    @Test
    void getUserById_nonExistsId_throwsUserNotFoundEx() {
        User expectedUser = getUser();

        when(userRepository.findById(ID))
                .thenReturn(Optional.empty());

        assertThatThrownBy(() -> userService.getUserById(ID))
                .isInstanceOf(UserNotFoundException.class);

        verify(userRepository).findById(ID);
    }

    private SignUpDtoRequest getSignUpDto() {
        return new SignUpDtoRequest("test-username", "test-pass", "test-pass");
    }

    private User getUser() {
        User user = new User();

        user.setId(ID);
        user.setUsername("test-username");
        user.setPassword("test-password");

        return user;
    }
}
