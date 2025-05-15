package com.ecommerce.neighboursnackbe.entity;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.Table;
import lombok.*;

@Entity
@Table(name = "unverified_users")
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class UnVerifiedUser extends BaseEntity {

    @Column(nullable = false, length = 50)
    private String name;

    @Column(nullable = false, length = 320, unique = true)
    private String email;

}
