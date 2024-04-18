package com.app.flashcards.unit.factory;

import com.app.flashcards.dto.auth.SignUpDto;
import com.app.flashcards.entity.User;
import com.app.flashcards.factory.UserFactory;
import com.app.flashcards.factory.UserFactoryImpl;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.Mockito;
import org.springframework.security.crypto.password.PasswordEncoder;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.Mockito.when;


public class UserFactoryTest {
    private UserFactory userFactory;

    @BeforeEach
    void setUp() {
        userFactory = new UserFactoryImpl();
    }

    @Test
    void createUserFromSignUp_withEncoder_createsUser() {
        SignUpDto signUpDto = new SignUpDto("test-username", "test-pass", "test-pass");
        String encodedPassword = "test-password";
        PasswordEncoder encoder = Mockito.mock(PasswordEncoder.class);

        when(encoder.encode(signUpDto.getPassword()))
                .thenReturn(encodedPassword);

        User user = userFactory.createFromSignUp(signUpDto, encoder);

        assertThat(user.getUsername()).isEqualTo(signUpDto.getUsername());
        assertThat(user.getPassword()).isEqualTo(encodedPassword);
    }

}
