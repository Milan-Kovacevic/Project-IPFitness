package dev.projectfitness.ipfitness.services;

import org.springframework.core.io.Resource;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.util.Optional;

public interface StorageAccessService {

    Optional<String> saveFile(String originalFileName, byte[] fileContent);

    Optional<String> saveFile(MultipartFile file);

    boolean deleteFile(String fileName) throws IOException;

    Resource getFileAsResource(String fileName) throws IOException;
}
