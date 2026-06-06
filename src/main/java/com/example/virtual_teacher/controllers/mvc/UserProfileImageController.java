package com.example.virtual_teacher.controllers.mvc;

import com.example.virtual_teacher.exceptions.AuthenticationFailureException;
import com.example.virtual_teacher.exceptions.EntityNotFoundException;
import com.example.virtual_teacher.exceptions.UnauthorizedOperationException;
import com.example.virtual_teacher.helpers.AuthenticationHelper;
import com.example.virtual_teacher.models.ProfileImage;
import com.example.virtual_teacher.models.User;
import com.example.virtual_teacher.services.AccessControlService;
import com.example.virtual_teacher.services.contracts.UserService;
import jakarta.servlet.http.HttpSession;
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
    private final AuthenticationHelper authenticationHelper;
    private final AccessControlService accessControlService;

    @Autowired
    public UserProfileImageController(UserService userService,
                                      AuthenticationHelper authenticationHelper,
                                      AccessControlService accessControlService) {
        this.userService = userService;
        this.authenticationHelper = authenticationHelper;
        this.accessControlService = accessControlService;
    }

    @GetMapping("/{publicId}/profile-image")
    public ResponseEntity<?> getProfileImage(@PathVariable String publicId, HttpSession session) {
        try {
            User authUser = authenticationHelper.tryGetUser(session);
            User targetUser = userService.getByPublicId(publicId);
            accessControlService.assertCanViewProfileImage(authUser, targetUser);
            ProfileImage profileImage = userService.getProfileImage(targetUser.getId());
            if (profileImage != null) {
                return ResponseEntity.ok()
                        .contentType(MediaType.parseMediaType(profileImage.contentType()))
                        .cacheControl(CacheControl.noCache().mustRevalidate())
                        .body(profileImage.data());
            }
            return ResponseEntity.status(HttpStatus.FOUND)
                    .location(URI.create(DEFAULT_IMAGE_URL))
                    .cacheControl(CacheControl.maxAge(1, TimeUnit.HOURS))
                    .build();
        } catch (AuthenticationFailureException e) {
            return ResponseEntity.status(HttpStatus.FOUND).location(URI.create("/auth/login")).build();
        } catch (EntityNotFoundException e) {
            return ResponseEntity.notFound().build();
        } catch (UnauthorizedOperationException e) {
            return ResponseEntity.status(HttpStatus.FORBIDDEN).build();
        }
    }
}
