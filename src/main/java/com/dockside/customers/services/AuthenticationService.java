package com.dockside.customers.services;

import org.springframework.stereotype.Service;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.crypto.password.PasswordEncoder;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

import com.dockside.customers.Domain.Role;
import com.dockside.customers.Domain.User;
import com.dockside.customers.Domain.Authentication.*;
import com.dockside.customers.repositories.UsersRepository;
import com.dockside.customers.security.JWTService;

@Service
@RequiredArgsConstructor
@Slf4j
public class AuthenticationService {

    private final UsersRepository userRepository;
    private final PasswordEncoder passwordEncoder;
    private final  JWTService jwtService;
    private final AuthenticationManager authManager;

    public AuthenticationResponse register(RegisterRequest request){
        var user = User.builder()
            .user_name(request.getUser_name())
            .email(request.getEmail())
            .password(passwordEncoder.encode(request.getPassword()))
            .role(Role.USER).build();
        userRepository.save(user);
        var jwtToken = jwtService.generateToken(user);
        return AuthenticationResponse.builder().token(jwtToken).build();
    }
    
    public AuthenticationResponse authenticate(AuthenticationRequest request) {
        authManager.authenticate(new UsernamePasswordAuthenticationToken(request.getEmail(), request.getPassword()));
        var user = userRepository.findByEmail(request.getEmail()).orElseThrow(()->new UsernameNotFoundException("User not Found for Given Account"));
        log.info("User retrieved: {}", user);
        var jwtToken = jwtService.generateToken(user);
        return AuthenticationResponse.builder().token(jwtToken).build();
    }
}
