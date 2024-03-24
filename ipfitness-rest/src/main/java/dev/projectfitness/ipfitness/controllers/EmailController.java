package dev.projectfitness.ipfitness.controllers;

import dev.projectfitness.ipfitness.models.dtos.AttachmentEmail;
import dev.projectfitness.ipfitness.models.requests.AttachmentMailRequest;
import dev.projectfitness.ipfitness.services.EmailSenderService;
import jakarta.validation.Valid;
import jdk.jfr.ContentType;
import lombok.RequiredArgsConstructor;
import org.modelmapper.ModelMapper;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;

@RequiredArgsConstructor
@RestController
@RequestMapping("${ipfitness.base-url}/email")
public class EmailController {
    private final EmailSenderService emailSenderService;
    private final ModelMapper modelMapper;

    @PostMapping
    @ResponseStatus(HttpStatus.OK)
    public void sendEmail(@ModelAttribute @Valid AttachmentMailRequest request) {
        AttachmentEmail email = modelMapper.map(request, AttachmentEmail.class);
        emailSenderService.sendEmailWithAttachment(email);
    }
}
