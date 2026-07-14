package com.stiveen.gestionhospitalaria.controller;

import com.stiveen.gestionhospitalaria.dto.request.LoginRequest;
import com.stiveen.gestionhospitalaria.dto.response.LoginResponse;
import com.stiveen.gestionhospitalaria.service.AuthService;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/auth")
public class AuthController {

    private final AuthService authService;

    public AuthController(AuthService authService) {
        this.authService = authService;
    }

    @PostMapping("/login")
    public ResponseEntity<LoginResponse> login(
            @RequestBody LoginRequest request
    ) {

        return ResponseEntity.ok(
                authService.login(request)
        );
    }
}