package com.ecommerce.neighboursnackbe.dto;

import com.ecommerce.neighboursnackbe.entity.Category;
import com.ecommerce.neighboursnackbe.entity.Product;
import com.ecommerce.neighboursnackbe.entity.ProductVariant;
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
            @NotBlank(message = "{product.variant.packSize.required}")
            @Size(max = 20, message = "{product.variant.packSize.maxSize}")
            String packSize,

            @NotNull(message = "{product.variant.price.required}")
            @Positive(message = "{product.variant.price.positive}")
            @Digits(integer = 10, fraction = 2, message = "{product.variant.price.digits}")
            BigDecimal price,

            @NotNull(message = "{product.variant.inStock.required}")
            Boolean inStock
    ) {
        public ProductVariant toEntity(Product product) {
            return ProductVariant.builder()
                    .packSize(packSize)
                    .price(price)
                    .inStock(inStock)
                    .product(product)
                    .build();
        }
    }

    public record ProductVariantResponseDTO(
            UUID uuid,
            String packSize,
            BigDecimal price,
            boolean inStock
    ) {
        public static ProductVariantResponseDTO fromEntity(ProductVariant variant) {
            return new ProductVariantResponseDTO(
                    variant.getUuid(),
                    variant.getPackSize(),
                    variant.getPrice(),
                    variant.getInStock()
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
                    product.getProductImage(),
                    product.getCategory().getName(),
                    product.getCategory().getUuid(),
                    product.getVariants().stream()
                            .map(ProductVariantResponseDTO::fromEntity)
                            .collect(Collectors.toList())
            );
        }
    }

}

