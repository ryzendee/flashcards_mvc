package com.app.flashcards.unit.controller;

import com.app.flashcards.dto.request.SignUpDtoRequest;
import com.app.flashcards.service.user.UserService;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.Arguments;
import org.junit.jupiter.params.provider.MethodSource;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.security.test.context.support.WithMockUser;
import org.springframework.test.web.servlet.MockMvc;

import java.util.stream.Stream;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.verify;
import static org.springframework.security.test.web.servlet.request.SecurityMockMvcRequestBuilders.formLogin;
import static org.springframework.security.test.web.servlet.request.SecurityMockMvcRequestPostProcessors.csrf;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@SpringBootTest
@AutoConfigureMockMvc
@ExtendWith(MockitoExtension.class)
public class AuthControllerTest {

    private static final String USERNAME = "test-username";
    private static final String PASSWORD = "test-password";

    @Autowired
    private MockMvc mockMvc;

    @MockBean
    private UserService authService;

    @Test
    void getLoginView_withView_returnLoginView() throws Exception {
        mockMvc.perform(
                get("/login")
        ).andExpectAll(
                status().isOk(),
                view().name("auth/login-view")
        );
    }

    //TODO
    @WithMockUser(username = USERNAME, password = PASSWORD)
    @Test
    void login_existsUser_redirectToModules() throws Exception {
        mockMvc.perform(
                post("/login")
                        .param("username", USERNAME)
                        .param("password", PASSWORD)
        ).andExpectAll(
                status().is3xxRedirection(),
                redirectedUrl("/flashcard-folder")
        );
    }

    @Test
    void login_nonExistsUser_redirectToLoginError() throws Exception {
        mockMvc.perform(
                formLogin("/login")
                        .user(USERNAME)
                        .password(PASSWORD)
        ).andExpectAll(
                status().is3xxRedirection(),
                redirectedUrl("/login?error")
        );
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
