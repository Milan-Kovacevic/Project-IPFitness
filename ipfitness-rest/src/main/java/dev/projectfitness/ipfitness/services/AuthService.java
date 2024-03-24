package dev.projectfitness.ipfitness.services;

import dev.projectfitness.ipfitness.models.dtos.LoginResponse;
import dev.projectfitness.ipfitness.models.requests.LoginRequest;
import dev.projectfitness.ipfitness.models.requests.RegistrationRequest;

public interface AuthService {
    public LoginResponse login(LoginRequest request);

    void register(RegistrationRequest request);

    void verify(String tokenId);
}
