package com.ecommerce.neighboursnackbe.security;

import com.ecommerce.neighboursnackbe.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

import java.util.UUID;

@Service
@RequiredArgsConstructor
public class UserDetailsServiceImpl implements UserDetailsService {
    private final UserRepository userRepository;

    // Used during login (email + password)
    @Override
    public UserDetails loadUserByUsername(String email) throws UsernameNotFoundException {
        return new UserDetailsImpl(userRepository.findUserByEmail(email));
    }

    // Used during JWT validation (UUID from token)
    public UserDetails loadUserByUuid(UUID userUuid) throws UsernameNotFoundException {
        return new UserDetailsImpl(userRepository.findUserByUuid(userUuid));
    }
}
