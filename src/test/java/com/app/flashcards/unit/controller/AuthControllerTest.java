package com.app.flashcards.unit.controller;

import com.app.flashcards.config.SecurityConfig;
import com.app.flashcards.controller.AuthController;
import com.app.flashcards.dto.request.SignUpDtoRequest;
import com.app.flashcards.entity.User;
import com.app.flashcards.service.user.UserService;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.Arguments;
import org.junit.jupiter.params.provider.MethodSource;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.autoconfigure.thymeleaf.ThymeleafAutoConfiguration;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.context.annotation.Import;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.test.web.servlet.MockMvc;

import java.util.stream.Stream;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.eq;
import static org.mockito.Mockito.*;
import static org.springframework.security.test.web.servlet.request.SecurityMockMvcRequestBuilders.formLogin;
import static org.springframework.security.test.web.servlet.request.SecurityMockMvcRequestPostProcessors.csrf;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;


@WebMvcTest(AuthController.class)
@AutoConfigureMockMvc
@Import({ThymeleafAutoConfiguration.class, SecurityConfig.class})
@ExtendWith(MockitoExtension.class)
class AuthControllerTest {
    private static final String USERNAME = "test-username";
    private static final String PASSWORD = "test-password";

    @Autowired
    private MockMvc mockMvc;
    @MockBean
    private UserService authService;
    @MockBean
    private UserDetailsService userDetailsService;
    @MockBean
    private PasswordEncoder passwordEncoder;

    @Test
    void getLoginView_withView_returnLoginView() throws Exception {
        mockMvc.perform(
                get("/login")
        ).andExpect(status().isOk())
                .andExpect(view().name("auth/login-view"));
    }

    @Test
    void login_existsUser_redirectToFolders() throws Exception {
        User user = new User(USERNAME, PASSWORD);
        when(userDetailsService.loadUserByUsername(USERNAME))
                .thenReturn(user);
        when(passwordEncoder.matches(any(CharSequence.class), eq(PASSWORD)))
                .thenReturn(true);

        mockMvc.perform(
                post("/login")
                        .param("username", USERNAME)
                        .param("password", PASSWORD)
                        .with(csrf())
        ).andExpectAll(
                status().is3xxRedirection(),
                redirectedUrl("/folders")
        );

        verify(userDetailsService, atLeastOnce()).loadUserByUsername(USERNAME);
        verify(passwordEncoder).matches(any(CharSequence.class), eq(PASSWORD));
    }

    @Test
    void login_nonExistsUser_redirectToLoginError() throws Exception {
        when(userDetailsService.loadUserByUsername(USERNAME))
                .thenThrow(UsernameNotFoundException.class);

        mockMvc.perform(
                formLogin("/login")
                        .user(USERNAME)
                        .password(PASSWORD)
        ).andExpectAll(
                status().is3xxRedirection(),
                redirectedUrl("/login?error")
        );

        verify(userDetailsService, atLeastOnce()).loadUserByUsername(USERNAME);
    }
    @Test
    void getSignUpView_withView_returnSignUpView() throws Exception {
        mockMvc.perform(
                get("/signup")
        ).andExpectAll(
                status().isOk(),
                view().name("auth/signup-view"),
                model().attributeExists("signUpDtoRequest")
        );
    }

    @Test
    void signUp_validDto_redirectToLogin() throws Exception {
        mockMvc.perform(
                post("/signup")
                        .param("username", USERNAME)
                        .param("password", PASSWORD)
                        .param("passwordConfirmation", PASSWORD)
                        .with(csrf())
        ).andExpectAll(
                status().is3xxRedirection(),
                redirectedUrl("/login")
        );

        verify(authService).createUser(any(SignUpDtoRequest.class));
    }
    @MethodSource("getArgsForInvalidSignUp")
    @ParameterizedTest
    void signUp_invalidData_returnSignUpView(String username,
                                             String password,
                                             String passwordConfirmation) throws Exception {

        mockMvc.perform(
                post("/signup")
                        .param("username", username)
                        .param("password", password)
                        .param("passwordConfirmation", passwordConfirmation)
                        .with(csrf())
        ).andExpectAll(
                status().isOk(),
                view().name("auth/signup-view"),
                model().attributeExists("errors")
        );
    }

    static Stream<Arguments> getArgsForInvalidSignUp() {
        return Stream.of(
                //Scenario 1: blank username
                Arguments.of("", PASSWORD, PASSWORD),

                //Scenario 2: null username
                Arguments.of(null, PASSWORD, PASSWORD),

                //Scenario 3: invalid password length
                Arguments.of(USERNAME, "t", "t"),

                //Scenario 4: password and password confirmation not matching
                Arguments.of(USERNAME, PASSWORD, "invalid-password"),

                //Scenario 5: blank password
                Arguments.of("test-name", " ", " ")
        );
    }
}
