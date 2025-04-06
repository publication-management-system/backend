package com.pms.publicationmanagement.service.resource;

import jakarta.validation.constraints.NotNull;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.io.File;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.UUID;

@Service
@RequiredArgsConstructor
@Slf4j
public class ResourceService {

    @Value("${resource-config.upload-resource-path}")
    private String resourceDirectory;

    @Value("${resource-config.profile-image-prefix}")
    private String profileImagePrefix;

    public String uploadProfileImage(UUID id, @NotNull MultipartFile file) throws IOException {
        Path filePath = Paths.get(resourceDirectory, profileImagePrefix);

        if (!Files.exists(filePath)) {
            Files.createDirectories(filePath);
        }

        if (file == null || file.isEmpty() || file.getOriginalFilename() == null || file.getOriginalFilename().isEmpty()) {
            throw new IllegalArgumentException("File is null or empty");
        }

        String extension = file.getOriginalFilename().split("\\.")[1];
        String profileImageName = String.format("%s.%s", id, extension);
        File targetFile = new File(filePath.toString(), profileImageName);

        file.transferTo(targetFile);

        return profileImageName;
    }

    public byte[] getProfileImage(String imageUrl) throws IOException {
        Path filePath = Paths.get(resourceDirectory, profileImagePrefix, imageUrl);
        if (!Files.exists(filePath)) {
            return null;
        }

        return Files.readAllBytes(filePath);
    }
}
