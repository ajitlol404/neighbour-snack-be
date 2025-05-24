package com.ecommerce.neighboursnackbe.entity;

import jakarta.persistence.*;
import lombok.*;

import java.math.BigDecimal;

@Entity
@Table(name = "product_variants")
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class ProductVariant extends BaseEntity {

    @Column(nullable = false, precision = 10, scale = 2)
    private BigDecimal weightValue; // e.g., 250, 1, 1.25

    @Enumerated(EnumType.STRING)
    @Column(nullable = false, length = 2)
    private WeightUnit weightUnit;  // g or kg

    @Column(nullable = false, precision = 10, scale = 2)
    private BigDecimal price;

    @Column(nullable = false)
    private Integer stockQuantity;

    @ManyToOne
    @JoinColumn(name = "product_uuid")
    private Product product;

}
