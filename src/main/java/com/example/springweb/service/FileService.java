package com.example.springweb.service;

import com.example.springweb.exception.MainException;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.UUID;

@Service
public class FileService {
    private final Path ROOT_LOCATION = Paths.get("uploads");
    public String saveFile(MultipartFile file, String... directoryPrefix) throws MainException {
        if (directoryPrefix.length == 0) {
            directoryPrefix = new String[]{""};
        }
        try {
            String originalFilename = file.getOriginalFilename();
            if (originalFilename == null) {
                throw new RuntimeException("File name is null");
            }
            Path directory = ROOT_LOCATION.resolve(directoryPrefix[0]);
            if (Files.notExists(directory)) {
                Files.createDirectories(directory);
            }
            Path filePath = Path.of(directoryPrefix[0], generateName(file));
            Path absoluteFilePath = ROOT_LOCATION.resolve(filePath);
            Files.copy(file.getInputStream(), absoluteFilePath);
            return filePath.toString();
        } catch (IOException e) {
            throw new MainException("File can not be saved!");
        }
    }

    private String extractExtension(MultipartFile file) {
        String originalName = file.getOriginalFilename();
        if (originalName == null) {
            return "";
        }
        String[] split = originalName.split("\\.");
        return split.length > 1 ? ("." + split[split.length - 1]) : "";
    }

    private String generateName(MultipartFile file) {
        UUID uuid = UUID.randomUUID();
        String extension = extractExtension(file);
        return uuid + extension;
    }
}
