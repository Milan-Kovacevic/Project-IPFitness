package dev.projectfitness.ipfitness.controllers;

import dev.projectfitness.ipfitness.models.dtos.LoginResponse;
import dev.projectfitness.ipfitness.models.requests.LoginRequest;
import dev.projectfitness.ipfitness.models.requests.RegistrationRequest;
import dev.projectfitness.ipfitness.services.AuthService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;

import java.sql.SQLOutput;

@RestController
@RequiredArgsConstructor
@RequestMapping(path = "${ipfitness.base-url}/auth")
public class AuthController {
    private final AuthService authService;

    @PostMapping("login")
    @ResponseStatus(HttpStatus.OK)
    public LoginResponse login(@RequestBody @Valid LoginRequest request) {
        return authService.login(request);
    }

    @PostMapping("register")
    @ResponseStatus(HttpStatus.CREATED)
    public void register(@ModelAttribute @Valid RegistrationRequest request) {
        authService.register(request);
    }

    @GetMapping("verify")
    @ResponseStatus(HttpStatus.OK)
    public void verifyAccount(@RequestParam String token) {
        authService.verify(token);
    }
}
