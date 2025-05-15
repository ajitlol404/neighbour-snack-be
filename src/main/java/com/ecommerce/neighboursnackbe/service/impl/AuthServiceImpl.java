package com.ecommerce.neighboursnackbe.service.impl;

import com.ecommerce.neighboursnackbe.dto.AuthDTO;
import com.ecommerce.neighboursnackbe.dto.AuthDTO.SignInRequestDTO;
import com.ecommerce.neighboursnackbe.dto.JwtDTO.JwtResponse;
import com.ecommerce.neighboursnackbe.repository.UserRepository;
import com.ecommerce.neighboursnackbe.security.UserDetailsImpl;
import com.ecommerce.neighboursnackbe.security.jwt.JwtService;
import com.ecommerce.neighboursnackbe.service.AuthService;
import lombok.RequiredArgsConstructor;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class AuthServiceImpl implements AuthService {

    private final UserRepository userRepository;
    private final PasswordEncoder passwordEncoder;
    private final JwtService jwtService;
    private final AuthenticationManager authenticationManager;

    @Override
    public JwtResponse signin(SignInRequestDTO signInRequestDTO) {
        UsernamePasswordAuthenticationToken authenticationToken =
                new UsernamePasswordAuthenticationToken(
                        signInRequestDTO.email(),
                        signInRequestDTO.password()
                );

        authenticationManager.authenticate(authenticationToken);

        UserDetailsImpl userDetails = userRepository.findByEmail(signInRequestDTO.email())
                .map(UserDetailsImpl::new)
                .orElseThrow(() -> new UsernameNotFoundException("User not found"));

        String jwtToken = jwtService.generateToken(userDetails);

        return new JwtResponse(jwtToken);
    }

}
