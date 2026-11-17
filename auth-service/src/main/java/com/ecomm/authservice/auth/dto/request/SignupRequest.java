package com.ecomm.authservice.auth.dto.request;

import com.ecomm.authservice.auth.validation.PasswordMatches;
import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;

@PasswordMatches
public record SignupRequest(

        @NotBlank(message = "{auth.firstname.not_blank}")
        String firstName,

        @NotBlank(message = "{auth.lastname.not_blank}")
        String lastName,

        @NotBlank(message = "{auth.email.not_blank}")
        @Email(message = "{auth.email.invalid}")
        String email,

        @NotBlank(message = "{auth.password.not_blank}")
        @Size(min = 8, message = "{auth.password.size}")
        String password,

        @NotBlank(message = "{auth.password.retype.not_blank}")
        String confirmPassword
) {}
