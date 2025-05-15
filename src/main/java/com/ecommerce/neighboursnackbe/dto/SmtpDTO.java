package com.ecommerce.neighboursnackbe.dto;

import com.ecommerce.neighboursnackbe.entity.Smtp;
import com.ecommerce.neighboursnackbe.util.AppUtil;
import jakarta.validation.constraints.*;

import java.util.UUID;

import static com.ecommerce.neighboursnackbe.util.AppConstant.MAX_STRING_DEFAULT_SIZE;
import static com.ecommerce.neighboursnackbe.util.AppConstant.MIN_STRING_DEFAULT_SIZE;

public class SmtpDTO {

    public record SmtpRequestDTO(
            @NotBlank(message = "{smtp.name.required}")
            @Size(min = MIN_STRING_DEFAULT_SIZE, max = 100, message = "{smtp.name.size}")
            @Pattern(
                    regexp = "^[a-zA-Z0-9()\\-]+$",
                    message = "{smtp.name.pattern}"
            )
            String name,

            @NotBlank(message = "{smtp.host.required}")
            @Size(max = 255, message = "{smtp.host.size}")
            String host,

            @NotNull(message = "{smtp.port.required}")
            @Min(value = 1, message = "{smtp.port.min}")
            @Max(value = 65535, message = "{smtp.port.max}")
            Integer port,

            @NotBlank(message = "{smtp.username.required}")
            @Size(max = MAX_STRING_DEFAULT_SIZE, message = "{smtp.username.size}")
            String username,

            @NotBlank(message = "{smtp.password.required}")
            @Size(max = MAX_STRING_DEFAULT_SIZE, message = "{smtp.password.size}")
            String password,

            @NotNull(message = "{smtp.ssl.required}")
            Boolean isSsl,

            @NotNull(message = "{smtp.active.required}")
            Boolean isActive
    ) {
        public Smtp toEntity() {
            return Smtp.builder()
                    .name(name)
                    .host(host)
                    .port(port)
                    .username(username)
                    .password(AppUtil.encodeBase64(password))
                    .isSsl(isSsl)
                    .isActive(isActive)
                    .build();
        }

        public Smtp updateSmtp(Smtp smtp) {
            smtp.setName(name);
            smtp.setHost(host);
            smtp.setPort(port);
            smtp.setUsername(username);
            smtp.setPassword(password);
            smtp.setSsl(isSsl);
            smtp.setActive(isActive);
            return smtp;
        }
    }

    public record SmtpResponseDTO(
            UUID uuid,
            String name,
            String host,
            Integer port,
            String username,
            Boolean isSsl,
            Boolean isActive
    ) {
        public static SmtpResponseDTO fromEntity(Smtp smtp) {
            return new SmtpResponseDTO(
                    smtp.getUuid(),
                    smtp.getName(),
                    smtp.getHost(),
                    smtp.getPort(),
                    smtp.getUsername(),
                    smtp.isSsl(),
                    smtp.isActive()
            );
        }
    }

    public record SmtpToggleRequestDTO(boolean isActive) {
    }

}
