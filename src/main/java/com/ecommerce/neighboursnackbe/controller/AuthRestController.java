package com.ecommerce.neighboursnackbe.controller;

import com.ecommerce.neighboursnackbe.dto.AuthDTO.SignInRequestDTO;
import com.ecommerce.neighboursnackbe.dto.JwtDTO;
import com.ecommerce.neighboursnackbe.service.AuthService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

import static com.ecommerce.neighboursnackbe.util.AppConstant.BASE_API_PATH;
import static org.springframework.http.HttpStatus.CREATED;

@RestController
@RequiredArgsConstructor
public class AuthRestController {

    private final AuthService authService;

    @PostMapping(BASE_API_PATH + "/signin")
    public ResponseEntity<JwtDTO.JwtResponse> signin(@Valid @RequestBody SignInRequestDTO request) {
        return ResponseEntity.status(CREATED).body(authService.signin(request));
    }

}
