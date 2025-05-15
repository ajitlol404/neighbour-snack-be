package com.ecommerce.neighboursnackbe.dto;

import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.Pattern;

import java.time.ZonedDateTime;
import java.util.Optional;

public record CategoryFilterRequest(
        @Min(value = 0, message = "{category.filter.page.min}")
        Integer page,
        @Min(value = 1, message = "{category.filter.size.min}")
        Integer size,
        String sortBy,
        @Pattern(regexp = "asc|desc", flags = Pattern.Flag.CASE_INSENSITIVE,
                message = "{category.filter.sortDir.pattern}")
        String sortDir,
        String search,
        String name,
        Boolean isActive,
        ZonedDateTime createdFrom,
        ZonedDateTime createdTo
) {
    public CategoryFilterRequest {
        page = Optional.ofNullable(page).orElse(0);
        size = Optional.ofNullable(size).orElse(10);
        sortBy = Optional.ofNullable(sortBy).orElse("createdAt");
        sortDir = Optional.ofNullable(sortDir).orElse("desc");
    }
}
