package com.ecomm.authservice.auth.validation;

import com.ecomm.authservice.auth.dto.request.SignupRequest;
import jakarta.validation.ConstraintValidator;
import jakarta.validation.ConstraintValidatorContext;

public class PasswordMatchesValidator implements ConstraintValidator<PasswordMatches, Object> {

    @Override
    public boolean isValid(Object obj, ConstraintValidatorContext context) {
        SignupRequest user = (SignupRequest) obj;
        return user.password() != null && user.password().equals(user.confirmPassword());
    }
}

