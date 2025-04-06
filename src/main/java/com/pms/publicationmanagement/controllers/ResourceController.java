package com.pms.publicationmanagement.controllers;

import com.pms.publicationmanagement.model.user.User;
import com.pms.publicationmanagement.service.resource.ResourceService;
import com.pms.publicationmanagement.service.user.UserService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.security.SecurityRequirement;
import lombok.RequiredArgsConstructor;
import org.springframework.http.MediaType;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.UUID;

@RestController
@RequiredArgsConstructor
@RequestMapping("/resources")
public class ResourceController {

    private final ResourceService resourceService;


    @GetMapping(path = "/profile/{imageUrl}",
            produces = {MediaType.IMAGE_JPEG_VALUE, MediaType.IMAGE_GIF_VALUE, MediaType.IMAGE_PNG_VALUE})
    public byte[] getProfileImage(@PathVariable String imageUrl) {

        try {
            return resourceService.getProfileImage(imageUrl);
        } catch (Exception e) {
            return null;
        }
    }
}
