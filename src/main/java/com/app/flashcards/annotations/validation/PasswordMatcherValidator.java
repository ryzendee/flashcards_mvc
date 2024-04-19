package com.app.flashcards.annotations.validation;

import com.app.flashcards.dto.request.SignUpDtoRequest;
import jakarta.validation.ConstraintValidator;
import jakarta.validation.ConstraintValidatorContext;

public class PasswordMatcherValidator implements ConstraintValidator<PasswordMatcher, SignUpDtoRequest> {

    @Override
    public void initialize(PasswordMatcher constraintAnnotation) {
        ConstraintValidator.super.initialize(constraintAnnotation);
    }

    @Override
    public boolean isValid(SignUpDtoRequest value, ConstraintValidatorContext context) {
        return value.getPassword() != null && value.getPassword().equals(value.getPasswordConfirmation());
    }
}
