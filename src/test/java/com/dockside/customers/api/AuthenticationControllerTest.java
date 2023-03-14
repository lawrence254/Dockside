package com.dockside.customers.api;

import java.util.Map;
import java.util.Optional;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;

import com.dockside.customers.Domain.Role;
import com.dockside.customers.Domain.User;
import com.dockside.customers.Domain.Authentication.AuthenticationRequest;
import com.dockside.customers.Domain.Authentication.AuthenticationResponse;
import com.dockside.customers.Domain.Authentication.RegisterRequest;
import com.dockside.customers.repositories.UsersRepository;
import com.dockside.customers.services.AuthenticationService;
import com.fasterxml.jackson.databind.ObjectMapper;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertTrue;
import static org.mockito.Mockito.when;
import static org.mockito.BDDMockito.given;
import static org.mockito.Mockito.doNothing;
import static org.mockito.Mockito.doThrow;

import static org.assertj.core.api.Assertions.assertThat;

@ExtendWith(MockitoExtension.class)
class AuthenticationControllerTest {

    @Mock
    private AuthenticationService authService;

    @Mock
    private UsersRepository usersRepository;

    @InjectMocks
    private AuthenticationController authenticationController;

    private ObjectMapper objectMapper;

    @BeforeEach
    void setUp() {
        objectMapper = new ObjectMapper();
    }

    @Test
    void testRegister() {
        RegisterRequest registerRequest = new RegisterRequest();
        registerRequest.setUser_name("testuser");
        registerRequest.setEmail("testuser@test.com");
        registerRequest.setPassword("password");

        new AuthenticationResponse();
        AuthenticationResponse jwtToken = AuthenticationResponse.builder().token("abc123").build();
        User user = User.builder().user_name(registerRequest.getUser_name()).email(registerRequest.getEmail())
                .password(registerRequest.getPassword()).role(Role.USER).build();
        given(authService.register(registerRequest)).willReturn(jwtToken);
        given(usersRepository.findByEmail(registerRequest.getEmail())).willReturn(Optional.of(user));

        ResponseEntity<Map<String, Object>> responseEntity = authenticationController.register(registerRequest);
        assertThat(responseEntity.getStatusCode()).isEqualTo(HttpStatus.OK);

        Map<String, Object> response = responseEntity.getBody();
        assertThat(response).isNotNull();
        assertThat(response.get("token")).isEqualTo(jwtToken);
        assertThat(response.get("user_uuid")).isEqualTo(user.getUserid());
    }

    @Test
    void testAuthenticate() {
        AuthenticationRequest authenticationRequest = new AuthenticationRequest();
        authenticationRequest.setEmail("testuser@test.com");
        authenticationRequest.setPassword("password");

        String jwtToken = "abc123";
        given(authService.authenticate(authenticationRequest)).willReturn(new AuthenticationResponse(jwtToken));

        ResponseEntity<AuthenticationResponse> responseEntity = authenticationController
                .authenticate(authenticationRequest);
        assertThat(responseEntity.getStatusCode()).isEqualTo(HttpStatus.OK);

        AuthenticationResponse response = responseEntity.getBody();
        assertThat(response).isNotNull();
        assertThat(response.getToken()).isEqualTo(jwtToken);
    }
}
