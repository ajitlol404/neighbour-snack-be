package com.ecommerce.neighboursnackbe.dto;

import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;

import static com.ecommerce.neighboursnackbe.util.AppConstant.MAX_STRING_DEFAULT_SIZE;

public class AuthDTO {
    public record SignInRequestDTO(
            @NotNull(message = "{auth.signin.email.required}")
            @NotBlank(message = "{auth.signin.email.blank}")
            @Email(message = "{auth.signin.email.invalid}")
            @Size(max = 320, message = "{auth.signin.email.size}")
            String email,

            @NotNull(message = "{auth.signin.password.required}")
            @NotBlank(message = "{auth.signin.password.blank}")
            @Size(max = MAX_STRING_DEFAULT_SIZE, message = "{auth.signin.password.size}")
            String password
    ) {
    }
}