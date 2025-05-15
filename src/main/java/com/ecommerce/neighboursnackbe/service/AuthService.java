package com.ecommerce.neighboursnackbe.service;

import com.ecommerce.neighboursnackbe.dto.AuthDTO.SignInRequestDTO;
import com.ecommerce.neighboursnackbe.dto.JwtDTO;

public interface AuthService {
    JwtDTO.JwtResponse signin(SignInRequestDTO signInRequestDTO);
}
