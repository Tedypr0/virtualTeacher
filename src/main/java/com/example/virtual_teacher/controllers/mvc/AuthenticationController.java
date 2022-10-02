package com.example.virtual_teacher.controllers.mvc;

import com.example.virtual_teacher.exceptions.AuthenticationFailureException;
import com.example.virtual_teacher.exceptions.DuplicateEntityException;
import com.example.virtual_teacher.exceptions.EntityNotFoundException;
import com.example.virtual_teacher.helpers.AuthenticationHelper;
import com.example.virtual_teacher.models.User;
import com.example.virtual_teacher.models.dtos.LoginDto;
import com.example.virtual_teacher.models.dtos.RegisterDto;
import com.example.virtual_teacher.services.contracts.UserService;

import com.example.virtual_teacher.services.mappers.UserMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;

import javax.servlet.http.HttpSession;
import javax.validation.Valid;

@Controller
@RequestMapping("/auth")
public class AuthenticationController {

    private final UserService userService;
    private final AuthenticationHelper authenticationHelper;
    private final UserMapper userMapper;


    @Autowired
    public AuthenticationController(UserService userService, AuthenticationHelper authenticationHelper, UserMapper userMapper) {
        this.userService = userService;
        this.authenticationHelper = authenticationHelper;
        this.userMapper = userMapper;
    }

    @GetMapping("/login")
    public String showLoginPage(Model model) {
        model.addAttribute("login", new LoginDto());
        return "login";
    }


    @PostMapping("/login")
    public String handleLogin(@Valid @ModelAttribute("login") LoginDto login,
                              BindingResult bindingResult,
                              HttpSession session) {
        if (bindingResult.hasErrors()) {
            return "login";
        }

        try {
            User user = authenticationHelper.verifyAuthentication(login.getEmail(), login.getPassword());
            session.setAttribute("currentUser", user);
            session.setAttribute("isAdmin", user.isAdmin());
            session.setAttribute("isTeacher", user.isTeacher());
            session.setAttribute("isAppliedForTeacher",userService.applicationExists(user.getId()));
            session.setAttribute("currentUserEmail",user.getEmail());
            return "redirect:/";
        } catch (AuthenticationFailureException e) {
            bindingResult.rejectValue("email", "auth_error", e.getMessage());
            return "login";
        }
    }

    @GetMapping("/logout")
    public String handleLogout(HttpSession session) {
        session.removeAttribute("currentUser");
        session.removeAttribute("currentUserEmail");
        return "redirect:/";
    }

    @GetMapping("/register")
    public String showRegisterPage(Model model) {
        model.addAttribute("register", new RegisterDto());
        return "register";
    }

    @PostMapping("/register")
    public String handleRegister(@Valid @ModelAttribute("register") RegisterDto register,
                                 BindingResult bindingResult) {

        if (bindingResult.hasErrors()) {
            return "register";
        }

        if (!register.getPassword().equals(register.getPasswordConfirm())) {
            bindingResult.rejectValue("passwordConfirm", "password_error",
                    "Password confirmation should match password.");
            return "register";
        }

        try {
            User user = userMapper.dtoToObj(register);
            userService.create(user);
            return "redirect:/";
        } catch (DuplicateEntityException e) {
            bindingResult.rejectValue("email", "email_error", e.getMessage());
            return "register";
        }
    }
}
