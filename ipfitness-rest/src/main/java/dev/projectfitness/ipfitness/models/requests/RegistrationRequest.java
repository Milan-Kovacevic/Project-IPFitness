package dev.projectfitness.ipfitness.models.requests;

import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;
import lombok.Data;
import org.springframework.web.multipart.MultipartFile;

@Data
public class RegistrationRequest {
    @NotBlank
    private String firstName;
    @NotBlank
    private String lastName;
    @NotBlank
    @Size(min = 4, max = 64)
    private String username;
    @NotBlank
    @Size(max = 64)
    private String password;
    @NotBlank
    private String city;
    @NotBlank
    @Email
    private String mail;
    private MultipartFile picture;
}
