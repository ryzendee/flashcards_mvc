package com.app.flashcards.integration.service;

import com.app.flashcards.entity.User;
import com.app.flashcards.integration.ITBase;
import com.app.flashcards.repository.UserRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;

class UserDetailsServiceIT extends ITBase {

    @Autowired
    private UserRepository userRepository;
    @Autowired
    private UserDetailsService userDetailsService;

    @BeforeEach
    void setUp() {
        userRepository.deleteAll();
    }


    @Test
    void loadUserByUsername_existsUser_shouldLoad() {
        //given
        User user = new User();
        user.setUsername("test-username");
        user.setPassword("test-password");
        userRepository.save(user);

        //when
        UserDetails userDetails = userDetailsService.loadUserByUsername(user.getUsername());

        //then
        assertThat(userDetails.getUsername()).isEqualTo(user.getUsername());
    }

    @Test
    void loadUserByUsername_nonExistsUser_throwUsernameNotFoundEx() {
        String username = "dummy-username";

        assertThatThrownBy(() -> userDetailsService.loadUserByUsername(username))
                .isInstanceOf(UsernameNotFoundException.class);
    }
}
