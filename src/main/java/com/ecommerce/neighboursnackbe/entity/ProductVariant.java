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

    @Column(nullable = false, length = 20)
    private String packSize;  // e.g., "100gm", "1kg", "10pcs"

    @Column(nullable = false, precision = 10, scale = 2)
    private BigDecimal price;

    @Column(nullable = false)
    private Boolean inStock;

    @ManyToOne
    @JoinColumn(name = "product_uuid")
    private Product product;

}
