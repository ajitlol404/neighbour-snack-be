package com.ecommerce.neighboursnackbe.service;

import com.ecommerce.neighboursnackbe.dto.UserDTO.UserResponseDTO;

public interface UserService {

    boolean areThereAdminUsers();

    void createAdminUser();

    UserResponseDTO createUser(String name, String email, String password);

    boolean userExistsByEmail(String email);

}