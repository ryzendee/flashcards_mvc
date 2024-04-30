package com.app.flashcards.unit.factory;

import com.app.flashcards.dto.request.SignUpDtoRequest;
import com.app.flashcards.entity.User;
import com.app.flashcards.factory.user.UserFactory;
import com.app.flashcards.factory.user.UserFactoryImpl;
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
        SignUpDtoRequest signUpDtoRequest = new SignUpDtoRequest("test-username", "test-pass", "test-pass");
        String encodedPassword = "test-password";
        PasswordEncoder encoder = Mockito.mock(PasswordEncoder.class);

        when(encoder.encode(signUpDtoRequest.getPassword()))
                .thenReturn(encodedPassword);

        User user = userFactory.createFromSignUp(signUpDtoRequest, encoder);

        assertThat(user.getUsername()).isEqualTo(signUpDtoRequest.getUsername());
        assertThat(user.getPassword()).isEqualTo(encodedPassword);
    }

}
