package com.example.virtual_teacher.controllers.mvc;

import com.example.virtual_teacher.exceptions.AuthenticationFailureException;
import com.example.virtual_teacher.exceptions.DuplicateEntityException;
import com.example.virtual_teacher.exceptions.EntityNotFoundException;
import com.example.virtual_teacher.exceptions.UnauthorizedOperationException;
import com.example.virtual_teacher.helpers.AuthenticationHelper;
import com.example.virtual_teacher.models.MotivationalLetter;
import com.example.virtual_teacher.models.User;
import com.example.virtual_teacher.models.dtos.UpdateUserDto;
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

import javax.servlet.http.HttpSession;
import javax.validation.Valid;
import java.io.IOException;

@Controller
@RequestMapping("/users")
public class UserMvcController {
    private final UserService userService;
    private final AuthenticationHelper authenticationHelper;
    private final UserMapper userMapper;
    private final RoleService roleService;
    private final NoteService noteService;

    @Autowired
    public UserMvcController(UserService userService, AuthenticationHelper authenticationHelper, UserMapper userMapper, RoleService roleService, NoteService noteService) {
        this.userService = userService;
        this.authenticationHelper = authenticationHelper;
        this.userMapper = userMapper;
        this.roleService = roleService;
        this.noteService = noteService;
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
            model.addAttribute("users", userService.getAll());
            return "users";
        }
        return "access-denied";
    }

    @GetMapping("/{id}/update")
    public String showUserEditPage(@PathVariable int id, Model model, HttpSession session) {
        User authUser;
        try {
            authUser = authenticationHelper.tryGetUser(session);
        } catch (AuthenticationFailureException e) {
            return "redirect:/auth/login";
        }
        try {
            User user = userService.getById(id);
            UpdateUserDto updateUserDto;

            if (authUser.isAdmin()) {
                updateUserDto = userMapper.updateAdminUserToDto(new UpdateUserDto(), user);
                model.addAttribute("roles", roleService.getAll());
            } else if (authUser.getId() != id) {
                updateUserDto = userMapper.objToUserDto(new UpdateUserDto(), authUser);
            } else {
                return "access-denied";
            }

            model.addAttribute("userId", id);
            model.addAttribute("user", updateUserDto);
            return "update-user";
        } catch (EntityNotFoundException e) {
            model.addAttribute("error", e.getMessage());
            return "not-found";
        }
    }

    @PostMapping("/{id}/update")
    public String updateUser(@PathVariable int id,
                             @Valid @ModelAttribute("user") UpdateUserDto updateUserDto,
                             BindingResult errors,
                             Model model,
                             HttpSession session) {
        User authUser;
        try {
            authUser = authenticationHelper.tryGetUser(session);
        } catch (AuthenticationFailureException e) {
            return "redirect:/auth/login";
        }
        if (errors.hasErrors()) {
            return "update-user";
        }

        try {
            User originalUser = userService.getById(id);
            User updatedUser;
            if (authUser.isAdmin()) {
                updatedUser = userMapper.updateAdminDtoToUser(updateUserDto, originalUser);
            } else if (authUser.getId() == id) {
                updatedUser = userMapper.userDtoToObj(updateUserDto, authUser);
            } else {
                return "access-denied";
            }
            userService.update(authUser, originalUser);
            return "redirect:/";
        } catch (DuplicateEntityException e) {
            errors.rejectValue("email", "duplicate_user", e.getMessage());
            return "update-user";
        } catch (EntityNotFoundException e) {
            model.addAttribute("error", e.getMessage());
            return "not-found";
        }
    }

    @PostMapping("/{id}/update/uploadImage")
    public String uploadImage(@PathVariable int id,
                              @RequestParam("imageFile") MultipartFile multipartFile,
                              HttpSession session) {
        User authUser;
        try {
            authUser = authenticationHelper.tryGetUser(session);
        } catch (AuthenticationFailureException e) {
            return "redirect:/auth/login";
        }
        if (!authUser.isAdmin() && authUser.getId() != id) {
            return "access-denied";
        }
        try {
            User userToAddImg = userService.getById(id);
            userService.saveImage(multipartFile, userToAddImg);
        } catch (IOException e) {
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST);
        }
        return "redirect:/";
    }

    @PostMapping("/{id}/update/status")
    public String updateStatus(@PathVariable int id,
                               Model model,
                               HttpSession session) {
        User authUser;
        try {
            authUser = authenticationHelper.tryGetUser(session);
        } catch (AuthenticationFailureException e) {
            return "redirect:/auth/login";
        }
        try {
            if (authUser.isAdmin()) {
                User originalUser = userService.getById(id);
                originalUser.setActive(!originalUser.isActive());
                userService.update(authUser, originalUser);
                return "redirect:/users";
            }
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

    @PostMapping("/{id}/application")
    public String promoteUserToTeacher(@PathVariable int id,
                                       Model model, HttpSession session,
                                       @RequestParam("decision") String decision) {
        User authUser;
        try {
            authUser = authenticationHelper.tryGetUser(session);
        } catch (AuthenticationFailureException e) {
            return "redirect:/auth/login";
        }
        try {
            User userToUpdate = userService.getById(id);
            if (authUser.isAdmin()) {
                userService.deleteTeacherApplication(id);
                userToUpdate = userMapper.updateUserRoleToTeacher(userToUpdate, decision);
                userService.update(authUser, userToUpdate);
                return "redirect:/users/applications";
            }
            return "access-denied";
        } catch (EntityNotFoundException e) {
            model.addAttribute("error", e.getMessage());
            return "not-found";
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

    @PostMapping("/{id}/update/isDeleted")
    public String deleteUser(@PathVariable int id, Model model, HttpSession session) {
        User authUser;
        try {
            authUser = authenticationHelper.tryGetUser(session);
        } catch (AuthenticationFailureException e) {
            return "redirect:/auth/login";
        }

        try {
            if (authUser.isAdmin()) {
                User originalUser = userService.getById(id);
                originalUser.setDeleted(!originalUser.isDeleted());
                userService.update(authUser, originalUser);
                return "redirect:/users";
            }
        } catch (EntityNotFoundException | UnauthorizedOperationException e) {
            model.addAttribute("error", e.getMessage());
        }
        return "access-denied";
    }
}