package com.example.virtual_teacher.controllers.mvc;

import com.example.virtual_teacher.models.ProfileImage;
import com.example.virtual_teacher.services.contracts.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.CacheControl;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;

import java.net.URI;
import java.util.concurrent.TimeUnit;

@Controller
@RequestMapping("/users")
public class UserProfileImageController {

    private static final String DEFAULT_IMAGE_URL = "/userImages/defaultImg.jpg";

    private final UserService userService;

    @Autowired
    public UserProfileImageController(UserService userService) {
        this.userService = userService;
    }

    @GetMapping("/{id}/profile-image")
    public ResponseEntity<?> getProfileImage(@PathVariable int id) {
        ProfileImage profileImage = userService.getProfileImage(id);
        if (profileImage != null) {
            return ResponseEntity.ok()
                    .contentType(MediaType.parseMediaType(profileImage.contentType()))
                    .cacheControl(CacheControl.noCache().mustRevalidate())
                    .body(profileImage.data());
        }
        return ResponseEntity.status(HttpStatus.FOUND)
                .location(URI.create(DEFAULT_IMAGE_URL))
                .cacheControl(CacheControl.maxAge(1, TimeUnit.DAYS).cachePublic())
                .build();
    }
}
