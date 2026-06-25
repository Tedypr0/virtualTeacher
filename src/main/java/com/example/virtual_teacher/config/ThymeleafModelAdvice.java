package com.example.virtual_teacher.config;

import com.example.virtual_teacher.helpers.ImageUrlHelper;
import com.example.virtual_teacher.services.contracts.ContactService;
import com.example.virtual_teacher.services.contracts.UserService;
import com.example.virtual_teacher.models.User;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpSession;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ModelAttribute;

@ControllerAdvice
public class ThymeleafModelAdvice {

    private final UserService userService;
    private final ContactService contactService;

    @Autowired
    public ThymeleafModelAdvice(UserService userService, ContactService contactService) {
        this.userService = userService;
        this.contactService = contactService;
    }

    @ModelAttribute("requestURI")
    public String requestURI(HttpServletRequest request) {
        return request.getRequestURI();
    }

    @ModelAttribute("isAuthenticated")
    public boolean isAuthenticated(HttpSession session) {
        return session.getAttribute("currentUser") != null;
    }

    @ModelAttribute("enrolledView")
    public boolean enrolledView() {
        return false;
    }

    @ModelAttribute("enrolledCount")
    public int enrolledCount() {
        return 0;
    }

    @ModelAttribute("teacherApplicationsNumber")
    public int teacherApplicationsNumber() {
        return userService.getAllTeacherApplications().size();
    }

    @ModelAttribute("defaultAvatarImage")
    public String defaultAvatarImage() {
        return ImageUrlHelper.DEFAULT_AVATAR_PATH;
    }

    @ModelAttribute("defaultCourseImage")
    public String defaultCourseImage() {
        return ImageUrlHelper.DEFAULT_COURSE_PATH;
    }

    @ModelAttribute("unreadContactCount")
    public long unreadContactCount(HttpSession session) {
        User currentUser = (User) session.getAttribute("currentUser");
        if (currentUser == null) return 0L;
        Boolean isAdmin = (Boolean) session.getAttribute("isAdmin");
        Boolean isTeacher = (Boolean) session.getAttribute("isTeacher");
        if (Boolean.TRUE.equals(isAdmin) || Boolean.TRUE.equals(isTeacher)) {
            return contactService.countUnread(currentUser.getId());
        }
        return 0L;
    }
}
