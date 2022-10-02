package com.example.virtual_teacher.controllers.mvc;

import com.example.virtual_teacher.helpers.AuthenticationHelper;
import com.example.virtual_teacher.models.User;
import com.example.virtual_teacher.services.contracts.HomeworkService;
import com.example.virtual_teacher.services.contracts.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.RequestMapping;

import javax.servlet.http.HttpSession;

@Controller
@RequestMapping("/")
public class HomeMvcController {

    private final UserService userService;

    @Autowired
    public HomeMvcController(UserService userService) {
        this.userService = userService;
    }

    @ModelAttribute("isAuthenticated")
    public boolean populateIsAuthenticated(HttpSession session) {
        return session.getAttribute("currentUser") != null;
    }

    @ModelAttribute("currentUser")
    public Object populateUser(HttpSession session){
        return session.getAttribute("currentUser");
    }

    @GetMapping
    public String showHomePage(Model model) {
        model.addAttribute("userList", userService.getAll());
        model.addAttribute("teacherApplicationsNumber", userService.getAllTeacherApplications().size());
        return "index";
    }

}
