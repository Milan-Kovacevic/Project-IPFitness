package dev.projectfitness.ipfitness.models.requests;

import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotEmpty;
import jakarta.validation.constraints.NotNull;
import lombok.Data;
import lombok.EqualsAndHashCode;
import org.springframework.web.multipart.MultipartFile;

import java.util.List;

@Data
public class AttachmentMailRequest {
    @Email
    private String emailFrom;
    @Email
    private String sendTo;
    @NotBlank
    private String subject;
    @NotBlank
    private String body;
    private Boolean isHtml = false;
    private List<@NotNull MultipartFile> attachmentFiles;
}
