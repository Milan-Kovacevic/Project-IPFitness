package dev.projectfitness.ipfitness.services.impls;

import dev.projectfitness.ipfitness.exceptions.BadRequestException;
import dev.projectfitness.ipfitness.services.ActionLoggingService;
import dev.projectfitness.ipfitness.services.StorageAccessService;
import dev.projectfitness.ipfitness.util.Utility;
import jakarta.annotation.PostConstruct;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.core.io.Resource;
import org.springframework.core.io.UrlResource;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.io.File;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.nio.file.StandardOpenOption;
import java.util.Base64;
import java.util.Optional;

@Service
@RequiredArgsConstructor
public class StorageAccessServiceImpl implements StorageAccessService {
    @Value("${ipfitness.storage.path}")
    private String storagePath;
    private final ActionLoggingService actionLoggingService;

    private Path rootPath;

    @PostConstruct
    private void postConstruct() {
        rootPath = Paths.get(storagePath).normalize().toAbsolutePath();
        File rootFolder = rootPath.toFile();
        if (!rootFolder.exists()) {
            try {
                Files.createDirectories(rootPath);
            } catch (IOException e) {
                actionLoggingService.logException(e);
            }
        }
    }

    @Override
    public synchronized Optional<String> saveFile(String originalFileName, byte[] fileContent) {
        String name = new File(originalFileName).getName();
        String extension = name.substring(name.lastIndexOf("."));
        String fileName = null;
        try {
            Path filePath = resolveNewFilePath(extension);
            Files.write(filePath, fileContent, StandardOpenOption.CREATE_NEW);
            fileName = filePath.toFile().getName();
        } catch (IOException e) {
            actionLoggingService.logException(e);
        }
        return Optional.ofNullable(fileName);
    }

    @Override
    public Optional<String> saveFile(MultipartFile file) {
        String originalFileName;
        byte[] fileContent;
        try {
            fileContent = file.getBytes();
            originalFileName = file.getOriginalFilename();
        } catch (IOException e) {
            actionLoggingService.logException(e);
            return Optional.empty();
        }

        return saveFile(originalFileName, fileContent);
    }

    @Override
    public boolean deleteFile(String fileName) throws IOException {
        Optional<Path> resolvedPath = resolveFilePath(fileName);
        if (resolvedPath.isEmpty())
            return false;
        Files.delete(resolvedPath.get());
        return true;
    }

    @Override
    public Resource getFileAsResource(String fileName) throws IOException {
        Optional<Path> resolvedPath = resolveFilePath(fileName);
        if(resolvedPath.isEmpty())
            throw new IOException("Unable to resolve path for a given file name.");

        return new UrlResource(resolvedPath.get().toUri());
    }

    // Resolves the path for a given file name and checks if it exists.
    // Returns optional path object.
    private Optional<Path> resolveFilePath(String fileName) {
        Path sourcePath = Paths.get(fileName);
        Path resolvedPath = rootPath.resolve(sourcePath).normalize().toAbsolutePath();

        if (!resolvedPath.getParent().equals(rootPath.toAbsolutePath()) || !Files.exists(resolvedPath))
            resolvedPath = null;
        return Optional.ofNullable(resolvedPath);
    }

    private Path resolveNewFilePath(String extension) throws IOException {
        String uniqueFileName = Utility.getUtilCurrentDate().getTime() + "_IPF";
        String encodedFileName = Base64.getEncoder().encodeToString(uniqueFileName.getBytes());
        return Path.of(rootPath.toFile().getPath(), encodedFileName + extension);
    }
}
