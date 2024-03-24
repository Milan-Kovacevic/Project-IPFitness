package dev.projectfitness.ipfitness.controllers;

import dev.projectfitness.ipfitness.exceptions.BadRequestException;
import dev.projectfitness.ipfitness.exceptions.NotFoundException;
import dev.projectfitness.ipfitness.services.ActionLoggingService;
import dev.projectfitness.ipfitness.services.StorageAccessService;
import jakarta.validation.constraints.NotEmpty;
import jakarta.validation.constraints.NotNull;
import lombok.RequiredArgsConstructor;
import org.springframework.core.io.Resource;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.util.Optional;

@RequiredArgsConstructor
@RestController
@RequestMapping("${ipfitness.base-url}/storage")
public class StorageAccessController {
    private final StorageAccessService storageAccessService;

    @GetMapping
    public ResponseEntity<Resource> getPictureFile(@RequestParam("fileName") @NotEmpty String fileName) {
        final Resource resource;
        try {
            resource = storageAccessService.getFileAsResource(fileName);
        } catch (IOException e) {
            throw new NotFoundException();
        }

        if (resource == null || !resource.exists() || !resource.isReadable())
            throw new NotFoundException();
        return ResponseEntity
                .ok()
                .contentType(MediaType.IMAGE_JPEG)
                .body(resource);
    }

    @PostMapping
    public ResponseEntity<String> uploadPictureFile(@ModelAttribute @NotNull MultipartFile pictureFile) {
        final String originalFileName;
        final byte[] fileContent;
        try {
            fileContent = pictureFile.getBytes();
            originalFileName = pictureFile.getOriginalFilename();
        } catch (IOException e) {
            throw new BadRequestException("File format is invalid");
        }
        Optional<String> fileName = storageAccessService.saveFile(originalFileName, fileContent);
        return fileName.map(s -> ResponseEntity.ok().contentType(MediaType.TEXT_PLAIN).body(s)).orElseGet(() -> ResponseEntity.notFound().build());
    }
}
