package com.dockside.customers.services;

import org.springframework.stereotype.Service;

import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.crypto.password.PasswordEncoder;

import lombok.RequiredArgsConstructor;

import com.dockside.customers.Domain.Role;
import com.dockside.customers.Domain.User;
import com.dockside.customers.Domain.Authentication.*;
import com.dockside.customers.repositories.TokenRepository;
import com.dockside.customers.repositories.UsersRepository;
import com.dockside.customers.security.JWTService;

@Service
@RequiredArgsConstructor
public class AuthenticationService {

    private final UsersRepository userRepository;
    private final PasswordEncoder passwordEncoder;
    private final  JWTService jwtService;
    private final AuthenticationManager authManager;
    private final TokenRepository tokenRepository;

    public AuthenticationResponse register(RegisterRequest request){
        var user = User.builder()
            .user_name(request.getUser_name())
            .email(request.getEmail())
            .password(passwordEncoder.encode(request.getPassword()))
            .role(Role.USER).build();
        userRepository.save(user);
        var jwtToken = jwtService.generateToken(user);
        var user_id = user.getUserid();
        saveUserToken(user, jwtToken);
        AuthenticationResponse response = new AuthenticationResponse(jwtToken, user_id);
        return response;
    }
    
    public AuthenticationResponse authenticate(AuthenticationRequest request) {
        authManager.authenticate(new UsernamePasswordAuthenticationToken(request.getEmail(), request.getPassword()));
        var user = userRepository.findByEmail(request.getEmail()).orElseThrow(()->new UsernameNotFoundException("User not Found for Given Account"));
        var jwtToken = jwtService.generateToken(user);
        revokeAllUserTokens(user);
        saveUserToken(user, jwtToken);
        AuthenticationResponse response = new AuthenticationResponse(jwtToken, user.getUserid());
        return response;
    }

    private void saveUserToken(User user, String jwtToken){
        var token = Token.builder().user(user).token(jwtToken).tokenType(TokenType.BEARER).expired(false).revoked(false).build();
        tokenRepository.save(token);
    }

    private void revokeAllUserTokens(User user){
        var validTokens = tokenRepository.findAllValidTokensByUser(user.getUserid());
        if (validTokens.isEmpty()) return;
        validTokens.forEach(token->{token.setExpired(true); token.setRevoked(true);});
        tokenRepository.saveAll(validTokens);
    }
}
