package com.app.flashcards.annotations.validation;

import com.app.flashcards.dto.auth.SignUpDto;
import jakarta.validation.ConstraintValidator;
import jakarta.validation.ConstraintValidatorContext;

public class PasswordMatcherValidator implements ConstraintValidator<PasswordMatcher, SignUpDto> {

    @Override
    public void initialize(PasswordMatcher constraintAnnotation) {
        ConstraintValidator.super.initialize(constraintAnnotation);
    }

    @Override
    public boolean isValid(SignUpDto value, ConstraintValidatorContext context) {
        return value.getPassword() != null && value.getPassword().equals(value.getPasswordConfirmation());
    }
}
