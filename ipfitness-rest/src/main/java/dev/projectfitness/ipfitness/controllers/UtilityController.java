package dev.projectfitness.ipfitness.controllers;

import lombok.RequiredArgsConstructor;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.web.bind.annotation.*;

@RestController
@RequiredArgsConstructor
@RequestMapping("/utility")
public class UtilityController {
    private final PasswordEncoder passwordEncoder;

    @PostMapping("/encrypt")
    public String encryptString(@RequestBody String text) {
        return passwordEncoder.encode(text);
    }
}
