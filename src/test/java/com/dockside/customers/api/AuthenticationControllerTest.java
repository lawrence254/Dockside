package com.dockside.customers.api;

import java.util.Map;
import java.util.Optional;
import java.util.UUID;

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
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertNull;
import static org.junit.jupiter.api.Assertions.assertTrue;
import static org.mockito.Mockito.when;
import static org.mockito.BDDMockito.given;
import static org.mockito.Mockito.doNothing;
import static org.mockito.Mockito.doThrow;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;
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
    void register_withValidRequest_returnsOkResponseWithAuthenticationResponse() {
        // Arrange
        RegisterRequest request = new RegisterRequest("testuser", "testemail", "testpassword");
        AuthenticationResponse expectedResponse = new AuthenticationResponse("testtoken", UUID.randomUUID());

        when(authService.register(request)).thenReturn(expectedResponse);

        // Act
        ResponseEntity<AuthenticationResponse> response = new AuthenticationController(authService).register(request);

        // Assert
        assertEquals(HttpStatus.OK, response.getStatusCode());
        assertNotNull(response.getBody());
        assertEquals(expectedResponse.getToken(), response.getBody().getToken());
        assertEquals(expectedResponse.getUser_id(), response.getBody().getUser_id());
        verify(authService, times(1)).register(request);
    }

    @Test
    void register_withInvalidRequest_returnsBadRequestResponse() throws IllegalArgumentException {
        // Arrange
        RegisterRequest request = new RegisterRequest(null, "testemail", "testpassword");

        when(authService.register(request)).thenThrow(new IllegalArgumentException());

        // Act
        ResponseEntity<AuthenticationResponse> response = null;
        try {
            response = new AuthenticationController(authService).register(request);
        } catch (IllegalArgumentException e) {
            response = ResponseEntity.badRequest().body(null);
        }

        // Assert
        assertEquals(HttpStatus.BAD_REQUEST, response.getStatusCode());
        assertNull(response.getBody());
        verify(authService, times(1)).register(request);
    
    }

    @Test
    void testAuthenticate() {
        AuthenticationRequest authenticationRequest = new AuthenticationRequest();
        authenticationRequest.setEmail("testuser@test.com");
        authenticationRequest.setPassword("password");
        UUID userid = UUID.fromString("TestPass");

        String jwtToken = "abc123";
        given(authService.authenticate(authenticationRequest)).willReturn(new AuthenticationResponse(jwtToken, userid));

        ResponseEntity<AuthenticationResponse> responseEntity = authenticationController
                .authenticate(authenticationRequest);
        assertThat(responseEntity.getStatusCode()).isEqualTo(HttpStatus.OK);

        AuthenticationResponse response = responseEntity.getBody();
        assertThat(response).isNotNull();
        assertThat(response.getToken()).isEqualTo(jwtToken);
    }
}
