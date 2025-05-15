package com.ecommerce.neighboursnackbe.dto;

import com.ecommerce.neighboursnackbe.entity.Category;
import com.ecommerce.neighboursnackbe.util.AppUtil;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Pattern;
import jakarta.validation.constraints.Size;

import java.time.ZonedDateTime;
import java.util.UUID;

import static com.ecommerce.neighboursnackbe.util.AppConstant.MIN_STRING_DEFAULT_SIZE;

public class CategoryDTO {

    public record CategoryRequestDTO(
            @NotBlank(message = "{category.name.required}")
            @Size(min = MIN_STRING_DEFAULT_SIZE, max = 100, message = "{category.name.size}")
            @Pattern(regexp = "^[A-Za-z0-9\\-'/()& ]+$", message = "{category.name.pattern}")
            String name,
            @Size(max = 500, message = "{category.description.size}")
            String description,
            @NotNull(message = "{category.status.required}")
            boolean isActive
    ) {
        public Category toEntity() {
            return Category.builder()
                    .name(name.toLowerCase())
                    .normalizedName(AppUtil.normalizeName(name))
                    .description(description)
                    .isActive(isActive)
                    .build();
        }

        public Category updateCategory(Category category) {
            category.setName(name.toLowerCase());
            category.setNormalizedName(AppUtil.normalizeName(name));
            category.setDescription(description);
            category.setActive(isActive);
            return category;
        }
    }

    public record CategoryResponseDTO(UUID uuid, String name, String description, String normalizedName,
                                      boolean isActive, ZonedDateTime createdAt, ZonedDateTime updatedAt) {
        public static CategoryResponseDTO fromEntity(Category category) {
            return new CategoryResponseDTO(
                    category.getUuid(),
                    category.getName(),
                    category.getDescription(),
                    category.getNormalizedName(),
                    category.isActive(),
                    category.getCreatedAt(),
                    category.getUpdatedAt());
        }
    }

    public record CategoryPublicResponseDTO(UUID uuid, String name, String normalizedName) {
        public static CategoryPublicResponseDTO fromEntity(Category category) {
            return new CategoryPublicResponseDTO(
                    category.getUuid(),
                    category.getName(),
                    category.getNormalizedName());
        }
    }

}
