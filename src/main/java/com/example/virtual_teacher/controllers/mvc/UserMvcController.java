package com.example.virtual_teacher.controllers.mvc;

import com.example.virtual_teacher.exceptions.AuthenticationFailureException;
import com.example.virtual_teacher.exceptions.DuplicateEntityException;
import com.example.virtual_teacher.exceptions.EntityNotFoundException;
import com.example.virtual_teacher.exceptions.UnauthorizedOperationException;
import com.example.virtual_teacher.helpers.AuthenticationHelper;
import com.example.virtual_teacher.models.MotivationalLetter;
import com.example.virtual_teacher.models.User;
import com.example.virtual_teacher.models.dtos.UpdateUserDto;
import com.example.virtual_teacher.services.AccessControlService;
import com.example.virtual_teacher.services.UserServiceImpl;
import com.example.virtual_teacher.services.contracts.NoteService;
import com.example.virtual_teacher.services.contracts.RoleService;
import com.example.virtual_teacher.services.contracts.UserService;
import com.example.virtual_teacher.services.mappers.UserMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.web.server.ResponseStatusException;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import jakarta.servlet.http.HttpSession;
import jakarta.validation.Valid;
import java.io.IOException;

@Controller
@RequestMapping("/users")
public class UserMvcController {
    private final UserService userService;
    private final AuthenticationHelper authenticationHelper;
    private final UserMapper userMapper;
    private final RoleService roleService;
    private final NoteService noteService;
    private final AccessControlService accessControlService;

    @Autowired
    public UserMvcController(UserService userService, AuthenticationHelper authenticationHelper,
                             UserMapper userMapper, RoleService roleService, NoteService noteService,
                             AccessControlService accessControlService) {
        this.userService = userService;
        this.authenticationHelper = authenticationHelper;
        this.userMapper = userMapper;
        this.roleService = roleService;
        this.noteService = noteService;
        this.accessControlService = accessControlService;
    }

    @ModelAttribute("isAuthenticated")
    public boolean populateIsAuthenticated(HttpSession session) {
        return session.getAttribute("currentUser") != null;
    }

    @ModelAttribute("currentUser")
    public Object populateUser(HttpSession session) {
        return session.getAttribute("currentUser");
    }

    @GetMapping
    public String showAllUsers(Model model, HttpSession session) {
        User authUser;
        try {
            authUser = authenticationHelper.tryGetUser(session);
        } catch (AuthenticationFailureException e) {
            return "redirect:/auth/login";
        }

        if (authUser.isAdmin()) {
            accessControlService.assertAdmin(authUser);
            model.addAttribute("users", userService.getAll());
            return "users";
        }
        return "access-denied";
    }

    @GetMapping("/{publicId}/update")
    public String showUserEditPage(@PathVariable String publicId, Model model, HttpSession session) {
        User authUser;
        try {
            authUser = authenticationHelper.tryGetUser(session);
        } catch (AuthenticationFailureException e) {
            return "redirect:/auth/login";
        }
        try {
            User user = userService.getByPublicId(publicId);
            accessControlService.assertCanViewUserProfile(authUser, user);
            UpdateUserDto updateUserDto;

            if (authUser.isAdmin()) {
                updateUserDto = userMapper.updateAdminUserToDto(new UpdateUserDto(), user);
                model.addAttribute("roles", roleService.getAll());
            } else {
                updateUserDto = userMapper.objToUserDto(new UpdateUserDto(), authUser);
            }

            model.addAttribute("publicId", publicId);
            model.addAttribute("user", updateUserDto);
            model.addAttribute("profileImageUrl", user.getImage());
            model.addAttribute("maxProfileImageBytes", UserServiceImpl.MAX_PROFILE_IMAGE_BYTES);
            return "update-user";
        } catch (EntityNotFoundException e) {
            model.addAttribute("error", e.getMessage());
            return "not-found";
        } catch (UnauthorizedOperationException e) {
            return "access-denied";
        }
    }

    @PostMapping(value = "/{publicId}/update", consumes = {"application/x-www-form-urlencoded", "multipart/form-data"})
    public String updateUser(@PathVariable String publicId,
                             @Valid @ModelAttribute("user") UpdateUserDto updateUserDto,
                             BindingResult errors,
                             @RequestParam(value = "imageFile", required = false) MultipartFile imageFile,
                             Model model,
                             HttpSession session,
                             RedirectAttributes redirectAttributes) {
        User authUser;
        try {
            authUser = authenticationHelper.tryGetUser(session);
        } catch (AuthenticationFailureException e) {
            return "redirect:/auth/login";
        }
        if (errors.hasErrors()) {
            populateUpdateUserModel(model, publicId, authUser, updateUserDto);
            return "update-user";
        }

        try {
            User originalUser = userService.getByPublicId(publicId);
            accessControlService.assertCanModifyUserProfile(authUser, originalUser);
            User updatedUser;
            if (authUser.isAdmin()) {
                updatedUser = userMapper.updateAdminDtoToUser(updateUserDto, originalUser);
            } else {
                updatedUser = userMapper.userDtoToObj(updateUserDto, authUser);
            }
            userService.update(authUser, updatedUser);
            if (imageFile != null && !imageFile.isEmpty()) {
                userService.saveImage(imageFile, updatedUser);
            }
            if (authUser.getId() == originalUser.getId()) {
                session.setAttribute("currentUser", userService.getByIdForSession(originalUser.getId()));
            }
            redirectAttributes.addFlashAttribute("profileSaved", true);
            return "redirect:/";
        } catch (ResponseStatusException e) {
            throw e;
        } catch (IOException e) {
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "Could not read image file.");
        } catch (DuplicateEntityException e) {
            errors.rejectValue("email", "duplicate_user", e.getMessage());
            populateUpdateUserModel(model, publicId, authUser, updateUserDto);
            return "update-user";
        } catch (EntityNotFoundException e) {
            model.addAttribute("error", e.getMessage());
            return "not-found";
        } catch (UnauthorizedOperationException e) {
            return "access-denied";
        }
    }

    private void populateUpdateUserModel(Model model, String publicId, User authUser, UpdateUserDto updateUserDto) {
        User user = userService.getByPublicId(publicId);
        model.addAttribute("publicId", publicId);
        model.addAttribute("user", updateUserDto);
        model.addAttribute("profileImageUrl", user.getImage());
        model.addAttribute("maxProfileImageBytes", UserServiceImpl.MAX_PROFILE_IMAGE_BYTES);
        if (authUser.isAdmin()) {
            model.addAttribute("roles", roleService.getAll());
        }
    }

    @PostMapping("/{publicId}/update/uploadImage")
    public String uploadImage(@PathVariable String publicId,
                              @RequestParam("imageFile") MultipartFile multipartFile,
                              HttpSession session,
                              RedirectAttributes redirectAttributes) {
        User authUser;
        try {
            authUser = authenticationHelper.tryGetUser(session);
        } catch (AuthenticationFailureException e) {
            return "redirect:/auth/login";
        }
        try {
            User userToAddImg = userService.getByPublicId(publicId);
            accessControlService.assertCanModifyUserProfile(authUser, userToAddImg);
            userService.saveImage(multipartFile, userToAddImg);
            User refreshedUser = userService.getByIdForSession(userToAddImg.getId());
            if (authUser.getId() == userToAddImg.getId()) {
                session.setAttribute("currentUser", refreshedUser);
            }
            redirectAttributes.addFlashAttribute("imageUploaded", true);
        } catch (ResponseStatusException e) {
            throw e;
        } catch (IOException e) {
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "Could not read image file.");
        } catch (EntityNotFoundException e) {
            return "not-found";
        } catch (UnauthorizedOperationException e) {
            return "access-denied";
        }
        return "redirect:/users/" + publicId + "/update";
    }

    @PostMapping("/{publicId}/update/status")
    public String updateStatus(@PathVariable String publicId,
                               Model model,
                               HttpSession session) {
        User authUser;
        try {
            authUser = authenticationHelper.tryGetUser(session);
        } catch (AuthenticationFailureException e) {
            return "redirect:/auth/login";
        }
        try {
            accessControlService.assertAdmin(authUser);
            User originalUser = userService.getByPublicId(publicId);
            originalUser.setActive(!originalUser.isActive());
            userService.update(authUser, originalUser);
            return "redirect:/users";
        } catch (EntityNotFoundException | UnauthorizedOperationException e) {
            model.addAttribute("error", e.getMessage());
        }
        return "access-denied";
    }

    @GetMapping("/applications")
    public String showAllTeacherApplications(Model model, HttpSession session) {
        User authUser;
        try {
            authUser = authenticationHelper.tryGetUser(session);
        } catch (AuthenticationFailureException e) {
            return "redirect:/auth/login";
        }

        if (authUser.isAdmin()) {
            accessControlService.assertAdmin(authUser);
            model.addAttribute("teacherApplications", userService.getAllTeacherApplications());
            model.addAttribute("teacherApplicationsNumber", userService.getAllTeacherApplications().size());
            return "teacher-applications";
        }
        return "access-denied";
    }

    @PostMapping("/applyForTeacher")
    public String applyForTeacher(Model model, HttpSession session,
                                  @RequestParam("motivationalLetter") String motivation) {

        User authUser;
        try {
            authUser = authenticationHelper.tryGetUser(session);
        } catch (AuthenticationFailureException e) {
            return "redirect:/auth/login";
        }
        try {
            MotivationalLetter letter = userMapper.motivationalLetterFieldToObj(authUser, motivation);
            userService.createTeacherApplication(authUser.getId(), letter);
            session.setAttribute("isAppliedForTeacher", true);
            return "redirect:/";
        } catch (EntityNotFoundException e) {
            model.addAttribute("error", e.getMessage());
            return "not-found";
        }
    }

    @PostMapping("/{publicId}/application")
    public String promoteUserToTeacher(@PathVariable String publicId,
                                       Model model, HttpSession session,
                                       @RequestParam("decision") String decision) {
        User authUser;
        try {
            authUser = authenticationHelper.tryGetUser(session);
        } catch (AuthenticationFailureException e) {
            return "redirect:/auth/login";
        }
        try {
            accessControlService.assertAdmin(authUser);
            User userToUpdate = userService.getByPublicId(publicId);
            userService.deleteTeacherApplication(userToUpdate.getId());
            userToUpdate = userMapper.updateUserRoleToTeacher(userToUpdate, decision);
            userService.update(authUser, userToUpdate);
            return "redirect:/users/applications";
        } catch (EntityNotFoundException e) {
            model.addAttribute("error", e.getMessage());
            return "not-found";
        } catch (UnauthorizedOperationException e) {
            return "access-denied";
        }
    }

    @GetMapping("/myNotes")
    public String showUserNotes(Model model, HttpSession session) {
        User authUser;
        try {
            authUser = authenticationHelper.tryGetUser(session);
        } catch (AuthenticationFailureException e) {
            return "redirect:/auth/login";
        }

        model.addAttribute("notes", noteService.getByUserId(authUser.getId()));
        return "user-notes";

    }

    @PostMapping("/{publicId}/update/isDeleted")
    public String deleteUser(@PathVariable String publicId, Model model, HttpSession session) {
        User authUser;
        try {
            authUser = authenticationHelper.tryGetUser(session);
        } catch (AuthenticationFailureException e) {
            return "redirect:/auth/login";
        }

        try {
            accessControlService.assertAdmin(authUser);
            User originalUser = userService.getByPublicId(publicId);
            originalUser.setDeleted(!originalUser.isDeleted());
            userService.update(authUser, originalUser);
            return "redirect:/users";
        } catch (EntityNotFoundException | UnauthorizedOperationException e) {
            model.addAttribute("error", e.getMessage());
        }
        return "access-denied";
    }
}