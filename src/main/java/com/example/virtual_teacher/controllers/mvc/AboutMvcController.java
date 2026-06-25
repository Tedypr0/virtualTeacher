package com.example.virtual_teacher.controllers.mvc;

import com.example.virtual_teacher.exceptions.UnauthorizedOperationException;
import com.example.virtual_teacher.models.User;
import com.example.virtual_teacher.models.dtos.ContactMessageDto;
import com.example.virtual_teacher.services.contracts.ContactService;
import com.example.virtual_teacher.services.contracts.CourseService;
import com.example.virtual_teacher.services.contracts.UserService;
import jakarta.servlet.http.HttpSession;
import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

@Controller
@RequestMapping("/about")
public class AboutMvcController {

    private final UserService userService;
    private final CourseService courseService;
    private final ContactService contactService;

    @Autowired
    public AboutMvcController(UserService userService, CourseService courseService, ContactService contactService) {
        this.userService = userService;
        this.courseService = courseService;
        this.contactService = contactService;
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
        model.addAttribute("teachers", userService.getTeachers());
        model.addAttribute("teacherApplicationsNumber", userService.getAllTeacherApplications().size());
        return "teacher";
    }

    @GetMapping("/teachers/{publicId}")
    public String showSingleTeacherPage(@PathVariable String publicId, Model model) {
        model.addAttribute("userList", userService.getAll());
        model.addAttribute("teacherApplicationsNumber", userService.getAllTeacherApplications().size());
        return "teacher-single";
    }

    @GetMapping("/contacts")
    public String showContactPage(Model model) {
        model.addAttribute("teacherApplicationsNumber", userService.getAllTeacherApplications().size());
        if (!model.containsAttribute("contactMessageDto")) {
            model.addAttribute("contactMessageDto", new ContactMessageDto());
        }
        return "contact";
    }

    @PostMapping("/contacts")
    public String submitContactMessage(@Valid @ModelAttribute("contactMessageDto") ContactMessageDto dto,
                                       BindingResult errors,
                                       RedirectAttributes redirectAttributes) {
        if (errors.hasErrors()) {
            redirectAttributes.addFlashAttribute("org.springframework.validation.BindingResult.contactMessageDto", errors);
            redirectAttributes.addFlashAttribute("contactMessageDto", dto);
            return "redirect:/about/contacts";
        }
        contactService.submit(dto);
        redirectAttributes.addFlashAttribute("successMessage", "Your message has been sent. We'll get back to you shortly.");
        return "redirect:/about/contacts";
    }

    @GetMapping("/contacts/messages")
    public String showContactMessages(HttpSession session, Model model) {
        if (session.getAttribute("isAdmin") == null && session.getAttribute("isTeacher") == null) {
            return "access-denied";
        }
        User currentUser = (User) session.getAttribute("currentUser");
        int userId = currentUser.getId();
        model.addAttribute("messages", contactService.getAll());
        model.addAttribute("readPublicIds", contactService.getReadPublicIds(userId));
        model.addAttribute("unreadCount", contactService.countUnread(userId));
        model.addAttribute("teacherApplicationsNumber", userService.getAllTeacherApplications().size());
        return "contact-messages";
    }

    @PostMapping("/contacts/messages/{publicId}/read")
    public String markAsRead(@PathVariable String publicId, HttpSession session) {
        if (session.getAttribute("isAdmin") == null && session.getAttribute("isTeacher") == null) {
            throw new UnauthorizedOperationException("Only admins and teachers can perform this action.");
        }
        User currentUser = (User) session.getAttribute("currentUser");
        contactService.markAsRead(publicId, currentUser.getId());
        return "redirect:/about/contacts/messages";
    }
}
