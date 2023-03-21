package com.dockside.customers.security;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertTrue;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.eq;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.never;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

import java.io.IOException;
import java.util.ArrayList;
import java.util.Optional;

import org.junit.jupiter.api.Disabled;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.ArgumentCaptor;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.mock.web.MockHttpServletRequest;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.User;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.web.authentication.WebAuthenticationDetails;

import com.dockside.customers.Domain.Authentication.Token;
import com.dockside.customers.repositories.TokenRepository;

import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;

@ExtendWith(MockitoExtension.class)
public class JwtAuthFilterTest {
    @Mock
    private JWTService jwtService;
    @Mock
    private UserDetailsService userDetailsService;
    @Mock
    private HttpServletRequest request;
    @Mock
    private HttpServletResponse response;
    @Mock
    private FilterChain filterChain;
    @Mock
    private Authentication authentication;
    @Mock
    private TokenRepository tokenRepository;

    @Mock
    private UserDetails userDetails;

    @InjectMocks
    private JwtAuthFilter jwtAuthFilter;

    @Disabled
    void testDoFilterInternalWithValidToken() throws Exception {
        // Arrange
        String userEmail = "test@example.com";
        UserDetails userDetails = new User(userEmail, "password", new ArrayList<>());
        String token = jwtService.generateToken(userDetails);

        // Set authorization header with a valid token
        when(request.getHeader("Authorization")).thenReturn("Bearer " + token);

        // Mock the user details service to return the user details
        when(userDetailsService.loadUserByUsername(userEmail)).thenReturn(userDetails);

        // Mock the token repository to return a valid token
        Token mockToken = mock(Token.class);
        when(mockToken.isExpired()).thenReturn(false);
        when(mockToken.isRevoked()).thenReturn(false);
        when(tokenRepository.findByToken(token)).thenReturn(Optional.of(mockToken));

        // Mock the JWT service to return that the token is valid
        when(jwtService.isTokenValid(token, userDetails)).thenReturn(true);

        // Act
        jwtAuthFilter.doFilterInternal(request, response, filterChain);

        // Assert
        ArgumentCaptor<UsernamePasswordAuthenticationToken> authenticationCaptor = ArgumentCaptor
                .forClass(UsernamePasswordAuthenticationToken.class);
        verify(filterChain).doFilter(eq(request), eq(response));
        // verify(filterChain).doFilter(authenticationCaptor.capture());
        Authentication authentication = authenticationCaptor.getValue();
        assertNotNull(authentication);
        assertEquals(userEmail, authentication.getPrincipal());
        assertTrue(authentication.isAuthenticated());
    }

    @Test
    void testDoFilterInternalWithInvalidToken() throws ServletException, IOException {
        String token = "invalid-token";
        String authHeader = "Bearer " + token;
        when(request.getHeader("Authorization")).thenReturn(authHeader);
        when(jwtService.extractUsername(token)).thenReturn(null);
        jwtAuthFilter.doFilterInternal(request, response, filterChain);
        verify(filterChain, times(1)).doFilter(request, response);
        verify(userDetailsService, never()).loadUserByUsername(any());
    }

    @Test
    void testDoFilterInternalWithMissingAuthorizationHeader() throws ServletException, IOException {
        when(request.getHeader("Authorization")).thenReturn(null);
        jwtAuthFilter.doFilterInternal(request, response, filterChain);
        verify(filterChain, times(1)).doFilter(request, response);
        verify(userDetailsService, never()).loadUserByUsername(any());
    }
}