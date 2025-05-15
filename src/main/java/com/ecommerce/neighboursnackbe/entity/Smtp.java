package com.ecommerce.neighboursnackbe.entity;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.Table;
import lombok.*;

@Entity
@Table(name = "smtps")
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class Smtp extends BaseEntity {

    @Column(nullable = false, unique = true, length = 100)
    private String name;

    @Column(nullable = false)
    private String host;

    @Column(nullable = false)
    private int port;

    @Column(nullable = false)
    private String username;

    @Column(nullable = false)
    private String password;

    @Column(nullable = false)
    private boolean isSsl;

    @Column(nullable = false)
    private boolean isActive;

}
