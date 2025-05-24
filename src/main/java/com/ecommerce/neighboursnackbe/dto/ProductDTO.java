package com.ecommerce.neighboursnackbe.dto;

import com.ecommerce.neighboursnackbe.entity.Category;
import com.ecommerce.neighboursnackbe.entity.Product;
import com.ecommerce.neighboursnackbe.entity.ProductVariant;
import com.ecommerce.neighboursnackbe.entity.WeightUnit;
import com.ecommerce.neighboursnackbe.util.AppUtil;
import jakarta.validation.constraints.*;

import java.math.BigDecimal;
import java.util.List;
import java.util.UUID;
import java.util.stream.Collectors;

import static com.ecommerce.neighboursnackbe.util.AppConstant.MIN_STRING_DEFAULT_SIZE;

public class ProductDTO {

    public record ProductRequestDTO(
            @NotBlank(message = "{product.name.required}")
            @Size(min = MIN_STRING_DEFAULT_SIZE, max = 150, message = "{product.name.size}")
            @Pattern(regexp = "^[A-Za-z0-9\\-'/()& ]+$", message = "{product.name.pattern}")
            String name,

            @Size(max = 500, message = "{product.description.size}")
            String description,

            @NotEmpty(message = "{product.variants.required}")
            @Size(max = 10, message = "{product.variants.maxSize}")
            List<ProductVariantRequestDTO> variants
    ) {
        public Product toEntity(Category category) {
            Product product = Product.builder()
                    .name(name.toLowerCase())
                    .normalizedName(AppUtil.normalizeName(name))
                    .description(description)
                    .category(category)
                    .build();

            List<ProductVariant> variantEntities = variants.stream()
                    .map(v -> v.toEntity(product))
                    .collect(Collectors.toList());

            product.setVariants(variantEntities);
            return product;
        }

        public Product applyUpdatesTo(Product product, Category category) {
            product.setName(name.toLowerCase());
            product.setNormalizedName(AppUtil.normalizeName(name));
            product.setDescription(description);
            product.setCategory(category);

            product.getVariants().clear();
            product.getVariants().addAll(
                    variants.stream().map(v -> v.toEntity(product)).toList()
            );
            return product;
        }
    }

    public record ProductVariantRequestDTO(
            @NotNull(message = "{product.variant.weightValue.required}")
            @Digits(integer = 10, fraction = 2, message = "{product.variant.weightValue.digits}")
            @Positive(message = "{product.variant.weightValue.positive}")
            BigDecimal weightValue,

            @NotNull(message = "{product.variant.weightUnit.required}")
            WeightUnit weightUnit,

            @NotNull(message = "{product.variant.price.required}")
            @Positive(message = "{product.variant.price.positive}")
            @Digits(integer = 10, fraction = 2, message = "{product.variant.price.digits}")
            BigDecimal price,

            @NotNull(message = "{product.variant.stockQuantity.required}")
            @PositiveOrZero(message = "{product.variant.stockQuantity.positiveOrZero}")
            Integer stockQuantity
    ) {
        public ProductVariant toEntity(Product product) {
            return ProductVariant.builder()
                    .weightValue(weightValue)
                    .weightUnit(weightUnit)
                    .price(price)
                    .stockQuantity(stockQuantity)
                    .product(product)
                    .build();
        }
    }

    public record ProductVariantResponseDTO(
            UUID uuid,
            BigDecimal weightValue,
            WeightUnit weightUnit,
            BigDecimal price,
            Integer stockQuantity
    ) {
        public static ProductVariantResponseDTO fromEntity(ProductVariant variant) {
            return new ProductVariantResponseDTO(
                    variant.getUuid(),
                    variant.getWeightValue(),
                    variant.getWeightUnit(),
                    variant.getPrice(),
                    variant.getStockQuantity()
            );
        }
    }

    public record ProductResponseDTO(
            UUID uuid,
            String name,
            String normalizedName,
            String description,
            String productImage,
            String categoryName,
            UUID categoryUuid,
            List<ProductVariantResponseDTO> variants
    ) {
        public static ProductResponseDTO fromEntity(Product product) {
            return new ProductResponseDTO(
                    product.getUuid(),
                    product.getName(),
                    product.getNormalizedName(),
                    product.getDescription(),
                    product.getImage(),
                    product.getCategory().getName(),
                    product.getCategory().getUuid(),
                    product.getVariants().stream()
                            .map(ProductVariantResponseDTO::fromEntity)
                            .collect(Collectors.toList())
            );
        }
    }

}

