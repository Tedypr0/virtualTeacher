package com.example.virtual_teacher.controllers.mvc;

import com.example.virtual_teacher.models.ProfileImage;
import com.example.virtual_teacher.services.contracts.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.io.ClassPathResource;
import org.springframework.http.CacheControl;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.util.StreamUtils;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;

import java.io.IOException;
import java.io.InputStream;
import java.util.concurrent.TimeUnit;

@Controller
@RequestMapping("/users")
public class UserProfileImageController {

    private static final String DEFAULT_IMAGE_PATH = "static/userImages/defaultImg.jpg";

    private final UserService userService;

    @Autowired
    public UserProfileImageController(UserService userService) {
        this.userService = userService;
    }

    @GetMapping("/{id}/profile-image")
    public ResponseEntity<byte[]> getProfileImage(@PathVariable int id) throws IOException {
        ProfileImage profileImage = userService.getProfileImage(id);
        if (profileImage != null) {
            return ResponseEntity.ok()
                    .contentType(MediaType.parseMediaType(profileImage.contentType()))
                    .cacheControl(CacheControl.maxAge(1, TimeUnit.HOURS).cachePublic())
                    .body(profileImage.data());
        }
        return defaultProfileImageResponse();
    }

    private ResponseEntity<byte[]> defaultProfileImageResponse() throws IOException {
        ClassPathResource resource = new ClassPathResource(DEFAULT_IMAGE_PATH);
        try (InputStream inputStream = resource.getInputStream()) {
            byte[] data = StreamUtils.copyToByteArray(inputStream);
            return ResponseEntity.ok()
                    .contentType(MediaType.IMAGE_JPEG)
                    .cacheControl(CacheControl.maxAge(1, TimeUnit.HOURS).cachePublic())
                    .body(data);
        }
    }
}
