package com.dockside.customers.api;

import lombok.RequiredArgsConstructor;

import java.util.HashMap;
import java.util.Map;
import java.util.Optional;

import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.http.ResponseEntity;

import com.dockside.customers.Domain.Authentication.AuthenticationResponse;
import com.dockside.customers.Domain.Authentication.RegisterRequest;
import com.dockside.customers.repositories.UsersRepository;
import com.dockside.customers.Domain.Authentication.AuthenticationRequest;
import com.dockside.customers.services.AuthenticationService;
import com.dockside.customers.Domain.User;


@RestController
@RequestMapping("/auth")
@RequiredArgsConstructor
public class AuthenticationController {

    private final AuthenticationService authService;
    private final UsersRepository repo;

    @PostMapping("/register")
    public ResponseEntity<Map<String, Object>> register(@RequestBody RegisterRequest request){
        var registerResult = authService.register(request);
        var id = repo.findByEmail(request.getEmail());
        HashMap<String, Object> result = new HashMap<>();
        result.put("token",registerResult);
        result.put("user_uuid", id.get().getUserid());
        return ResponseEntity.ok().body(result);
    }

    @PostMapping("/authenticate")
    public ResponseEntity<AuthenticationResponse> authenticate(@RequestBody AuthenticationRequest request){
        return ResponseEntity.ok(authService.authenticate(request));
    }
}
