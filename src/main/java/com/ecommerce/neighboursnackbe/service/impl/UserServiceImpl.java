package com.ecommerce.neighboursnackbe.service.impl;

import com.ecommerce.neighboursnackbe.dto.UserDTO;
import com.ecommerce.neighboursnackbe.entity.User;
import com.ecommerce.neighboursnackbe.repository.UserRepository;
import com.ecommerce.neighboursnackbe.service.UserService;
import lombok.RequiredArgsConstructor;
import org.springframework.core.io.Resource;
import org.springframework.core.io.UrlResource;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.nio.file.Files;
import java.nio.file.Path;
import java.util.List;
import java.util.NoSuchElementException;
import java.util.UUID;

import static com.ecommerce.neighboursnackbe.entity.Role.ROLE_ADMIN;
import static com.ecommerce.neighboursnackbe.entity.Role.ROLE_CUSTOMER;
import static com.ecommerce.neighboursnackbe.util.AppConstant.USER_IMAGE_DIRECTORY;
import static java.lang.Boolean.TRUE;

@Service
@RequiredArgsConstructor
public class UserServiceImpl implements UserService {

    private final UserRepository userRepository;
    private final PasswordEncoder passwordEncoder;

    @Override
    public boolean areThereAdminUsers() {
        return userRepository.existsByRole(ROLE_ADMIN);
    }

    @Override
    @Transactional
    public void createAdminUser() {
        if (!areThereAdminUsers()) {
            User admin = User.builder()
                    .name("Admin")
                    .email("admin@neighboursnack.com")
                    .password(passwordEncoder.encode("Admin@1234"))
                    .enabled(TRUE)
                    .role(ROLE_ADMIN)
                    .image(null)
                    .phoneNumber(null)
                    .userData(
                            User.UserData.builder()
                                    .secretKey(UUID.randomUUID())
                                    .secretKeyStatus(false)
                                    .build()
                    )
                    .addresses(List.of())
                    .build();
            userRepository.save(admin);
        }
    }

    @Override
    @Transactional
    public UserDTO.UserResponseDTO createUser(String name, String email, String password) {
        User customer = User.builder()
                .name(name.toLowerCase())
                .email(email.toLowerCase())
                .password(passwordEncoder.encode(password))
                .enabled(true)
                .role(ROLE_CUSTOMER)
                .image(null)
                .phoneNumber(null)
                .userData(
                        User.UserData.builder()
                                .secretKey(UUID.randomUUID())
                                .secretKeyStatus(false)
                                .build()
                )
                .addresses(List.of())
                .build();
        return UserDTO.UserResponseDTO.fromEntity(userRepository.save(customer));
    }

    @Override
    public boolean userExistsByEmail(String email) {
        return userRepository.existsByEmail(email);
    }

    @Override
    public Resource getUserImage(UUID userUuid) {
        User user = userRepository.findUserByUuid(userUuid);
        Path imagePath = USER_IMAGE_DIRECTORY.resolve(user.getImage());

        try {
            if (!Files.exists(imagePath)) {
                throw new NoSuchElementException("Image not found: " + user.getImage());
            }
            return new UrlResource(imagePath.toUri());
        } catch (Exception e) {
            throw new RuntimeException("Invalid file path for image: " + user.getImage(), e);
        }
    }
}
