package com.example.virtual_teacher.controllers.mvc;

import com.example.virtual_teacher.services.contracts.CourseService;
import com.example.virtual_teacher.services.contracts.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;

import javax.servlet.http.HttpSession;

@Controller
@RequestMapping("/about")
public class AboutMvcController {

    private final UserService userService;

    private final CourseService courseService;

    @Autowired
    public AboutMvcController(UserService userService, CourseService courseService) {
        this.userService = userService;
        this.courseService = courseService;
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
    public String showAboutPage(Model model) {
        model.addAttribute("userList", userService.getAll());
        model.addAttribute("teacherApplicationsNumber", userService.getAllTeacherApplications().size());
        model.addAttribute("teacherCount", userService.getAllTeachers());
        model.addAttribute("coursesCount", courseService.courseCount());
        model.addAttribute("studentCount", userService.getAllStudents());
        return "about";
    }

    @GetMapping("/teachers")
    public String showTeachersPage(Model model) {
        model.addAttribute("userList", userService.getAll());
        model.addAttribute("teacherApplicationsNumber", userService.getAllTeacherApplications().size());
        return "teacher";
    }

    @GetMapping("/teachers/{id}")
    public String showSingleTeacherPage(@PathVariable int id, Model model) {
        model.addAttribute("userList", userService.getAll());
        model.addAttribute("teacherApplicationsNumber", userService.getAllTeacherApplications().size());
        return "teacher-single";
    }

    @GetMapping("/contacts")
    public String showContactPage(Model model) {
        model.addAttribute("userList", userService.getAll());
        model.addAttribute("teacherApplicationsNumber", userService.getAllTeacherApplications().size());
        return "contact";
    }

}
