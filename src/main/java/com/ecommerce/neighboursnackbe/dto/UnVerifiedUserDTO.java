package com.ecommerce.neighboursnackbe.dto;

import com.ecommerce.neighboursnackbe.entity.UnVerifiedUser;
import com.ecommerce.neighboursnackbe.util.AppUtil;
import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Pattern;
import jakarta.validation.constraints.Size;

import java.util.UUID;

import static com.ecommerce.neighboursnackbe.util.AppConstant.MIN_STRING_DEFAULT_SIZE;
import static com.ecommerce.neighboursnackbe.util.AppConstant.PASSWORD_REGEX;

public class UnVerifiedUserDTO {

    public record UnVerifiedUserRequestDTO(
            @NotBlank(message = "{unverified.user.name.required}")
            @Size(min = MIN_STRING_DEFAULT_SIZE, max = 50, message = "{unverified.user.name.size}")
            @Pattern(regexp = "^[A-Za-z0-9\\-'/()& ]+$", message = "{unverified.user.name.pattern}")
            String name,

            @NotBlank(message = "{unverified.user.email.required}")
            @Email(message = "{unverified.user.email.invalid}")
            @Size(max = 320, message = "{unverified.user.email.size}")
            String email) {

        // Convert to entity
        public UnVerifiedUser toEntity() {
            return UnVerifiedUser.builder()
                    .name(name)
                    .email(email)
                    .build();
        }

        // Update entity with the values from the DTO (if needed)
        public UnVerifiedUser updateUnVerifiedUser(UnVerifiedUser unVerifiedUser) {
            unVerifiedUser.setName(name);
            unVerifiedUser.setEmail(email);
            return unVerifiedUser;
        }
    }

    public record UnVerifiedUserResponseDTO(UUID uuid, String name, String email) {

        // Convert entity to response DTO
        public static UnVerifiedUserResponseDTO fromEntity(UnVerifiedUser unVerifiedUser) {
            return new UnVerifiedUserResponseDTO(
                    unVerifiedUser.getUuid(),
                    unVerifiedUser.getName(),
                    AppUtil.maskedEmail(unVerifiedUser.getEmail()));
        }
    }

    public record UnVerifiedUserPublicResponseDTO(UUID uuid, String name, String email) {

        // Convert entity to public response DTO
        public static UnVerifiedUserPublicResponseDTO fromEntity(UnVerifiedUser unVerifiedUser) {
            return new UnVerifiedUserPublicResponseDTO(
                    unVerifiedUser.getUuid(),
                    unVerifiedUser.getName(),
                    AppUtil.maskedEmail(unVerifiedUser.getEmail())
            );
        }
    }

    public record UnVerifiedUserPasswordDTO(
            @NotBlank(message = "{unverified.user.password.required}")
            @Size(min = 8, max = 30, message = "{unverified.user.password.size}")
            @Pattern(regexp = PASSWORD_REGEX, message = "{unverified.user.password.pattern}")
            String password) {
    }

}