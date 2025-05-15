package com.ecommerce.neighboursnackbe.dto;

public record EmailRequestDTO(
        String toEmail,
        String subject,
        String bodyHtml
) {
}
