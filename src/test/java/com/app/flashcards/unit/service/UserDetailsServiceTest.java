package com.app.flashcards.unit.service;

import com.app.flashcards.entity.User;
import com.app.flashcards.repository.UserRepository;
import com.app.flashcards.service.user.UserDetailsServiceImpl;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UsernameNotFoundException;

import java.util.Optional;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
public class UserDetailsServiceTest {

    private static final String USERNAME = "test-username";
    private static final String PASSWORD = "test-password";
    @InjectMocks
    private UserDetailsServiceImpl userDetailsService;

    @Mock
    private UserRepository userRepository;

    @Test
    void loadByUsername_existsUsername_returnsUser() {
        User expectedUserDetails = getUserWithPasswordAndUsername();

        when(userRepository.findByUsername(USERNAME))
                .thenReturn(Optional.of(expectedUserDetails));

        UserDetails actualDetails = userDetailsService.loadUserByUsername(USERNAME);
        assertThat(actualDetails).isEqualTo(expectedUserDetails);

        verify(userRepository).findByUsername(USERNAME);
    }

    @Test
    void loadByUsername_nonExistsUsername_throwsUserNotFoundEx() {
        when(userRepository.findByUsername(USERNAME))
                .thenReturn(Optional.empty());

        assertThatThrownBy(() -> userDetailsService.loadUserByUsername(USERNAME))
                .isInstanceOf(UsernameNotFoundException.class);

        verify(userRepository).findByUsername(USERNAME);
    }

    private User getUserWithPasswordAndUsername() {
        User user = new User();
        user.setUsername(USERNAME);
        user.setPassword(PASSWORD);

        return user;
    }
}
