package com.app.flashcards.unit.annotation.validation;

import com.app.flashcards.annotations.validation.PasswordMatcher;
import com.app.flashcards.annotations.validation.PasswordMatcherValidator;
import com.app.flashcards.dto.request.SignUpDtoRequest;
import jakarta.validation.ConstraintValidator;
import jakarta.validation.ConstraintValidatorContext;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.Arguments;
import org.junit.jupiter.params.provider.MethodSource;
import org.junit.jupiter.params.provider.NullSource;
import org.mockito.Mockito;

import java.util.stream.Stream;

import static org.assertj.core.api.Assertions.assertThat;

public class PasswordMatcherValidatorTest {

    private ConstraintValidator<PasswordMatcher, SignUpDtoRequest> passwordMatcherValidator;
    private ConstraintValidatorContext mockContext;

    @BeforeEach
    void setUp() {
        passwordMatcherValidator = new PasswordMatcherValidator();
        mockContext = Mockito.mock(ConstraintValidatorContext.class);
    }

    @Test
    void isValid_validDtoRequest_returnsTrue() {
        SignUpDtoRequest validDtoRequest = new SignUpDtoRequest();
        validDtoRequest.setPassword("test-password");
        validDtoRequest.setPasswordConfirmation(validDtoRequest.getPassword());

        boolean result = passwordMatcherValidator.isValid(validDtoRequest, mockContext);
        assertThat(result).isTrue();
    }


    @MethodSource("getInvalidArgsForDtoRequest")
    @ParameterizedTest
    void isValid_invalidDtoRequest_returnsFalse(String password, String passwordConfirmation) {
        SignUpDtoRequest invalidDtoRequest = new SignUpDtoRequest();
        invalidDtoRequest.setPassword(password);
        invalidDtoRequest.setPasswordConfirmation(passwordConfirmation);

        boolean result = passwordMatcherValidator.isValid(invalidDtoRequest, mockContext);
        assertThat(result).isFalse();
    }

    //Format: Arguments.of(password, passwordConfirmation)
    static Stream<Arguments> getInvalidArgsForDtoRequest() {
        return Stream.of(
                //Scenario 1: Different passwords
                Arguments.of("password", "passwordConfirmation"),

                //Scenario 2: Password is null
                Arguments.of(null, "passwordConfirmation")
        );
    }
}
