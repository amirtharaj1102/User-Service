package com.coderank.userservice.service;

import com.coderank.userservice.dto.AuthResponse;
import com.coderank.userservice.dto.LoginRequest;
import com.coderank.userservice.dto.RegisterRequest;
import com.coderank.userservice.model.User;
import com.coderank.userservice.repository.UserRepository;
import com.coderank.userservice.util.JwtUtil;
import lombok.RequiredArgsConstructor;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.Set;

@Service
@RequiredArgsConstructor
public class AuthService {

    private final UserRepository userRepository;
    private final PasswordEncoder passwordEncoder;
    private final JwtUtil jwtUtil;

    @Transactional
    public AuthResponse signup(RegisterRequest request) {
        if (userRepository.existsByEmail(request.getEmail())) {
            throw new IllegalArgumentException("Email address already in use.");
        }

        User user = User.builder()
                .name(request.getName())
                .email(request.getEmail())
                .password(passwordEncoder.encode(request.getPassword()))
                .roles(Set.of("ROLE_USER"))
                .build();

        User savedUser = userRepository.save(user);
        String token = jwtUtil.generateToken(savedUser.getEmail(), savedUser.getRoles());

        return AuthResponse.builder()
                .token(token)
                .name(savedUser.getName())
                .email(savedUser.getEmail())
                .roles(savedUser.getRoles())
                .build();
    }

    @Transactional(readOnly = true)
    public AuthResponse login(LoginRequest request) {
        User user = userRepository.findByEmail(request.getEmail())
                .orElseThrow(() -> new BadCredentialsException("Invalid email or password."));

        if (!passwordEncoder.matches(request.getPassword(), user.getPassword())) {
            throw new BadCredentialsException("Invalid email or password.");
        }

        String token = jwtUtil.generateToken(user.getEmail(), user.getRoles());

        return AuthResponse.builder()
                .token(token)
                .name(user.getName())
                .email(user.getEmail())
                .roles(user.getRoles())
                .build();
    }

}
