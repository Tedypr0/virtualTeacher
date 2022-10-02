package com.example.virtual_teacher.controllers.mvc;

import com.example.virtual_teacher.exceptions.AuthenticationFailureException;
import com.example.virtual_teacher.exceptions.EntityNotFoundException;
import com.example.virtual_teacher.exceptions.UnauthorizedOperationException;
import com.example.virtual_teacher.helpers.AuthenticationHelper;
import com.example.virtual_teacher.models.Homework;
import com.example.virtual_teacher.models.User;
import com.example.virtual_teacher.services.contracts.CourseService;
import com.example.virtual_teacher.services.contracts.HomeworkService;
import com.example.virtual_teacher.services.mappers.HomeworkMapper;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpSession;
import java.util.List;

@Controller
@RequestMapping("/users")
public class HomeworkMvcController {

    private final HomeworkService homeworkService;
    private final CourseService courseService;
    private final AuthenticationHelper authenticationHelper;
    private final HomeworkMapper homeworkMapper;

    public HomeworkMvcController(HomeworkService homeworkService, CourseService courseService,
                                 AuthenticationHelper authenticationHelper, HomeworkMapper homeworkMapper) {
        this.homeworkService = homeworkService;
        this.courseService = courseService;
        this.authenticationHelper = authenticationHelper;
        this.homeworkMapper = homeworkMapper;
    }

    @ModelAttribute("isAuthenticated")
    public boolean populateIsAuthenticated(HttpSession session) {
        return session.getAttribute("currentUser") != null;
    }
    @ModelAttribute("currentUser")
    public Object populateUser(HttpSession session) {
        return session.getAttribute("currentUser");
    }

    @GetMapping("/myCourses")
    public String showAllCoursesByStudentId(HttpSession session, Model model) {

        User authUser;
        try {
            authUser = authenticationHelper.tryGetUser(session);
        } catch (AuthenticationFailureException e) {
            model.addAttribute("error", e.getMessage());
            return "redirect:/auth/login";
        }
        model.addAttribute("coursesByUserId", courseService.getByUserId(authUser.getId()));
        model.addAttribute("currentUser", authUser);
        return "my-courses-student";
    }

    @GetMapping("/myHomeworks")
    public String showAllHomeworksByStudentId(HttpSession session, Model model) {

        User authUser;
        try {
            authUser = authenticationHelper.tryGetUser(session);
        } catch (AuthenticationFailureException e) {
            model.addAttribute("error", e.getMessage());
            return "redirect:/auth/login";
        }
        List<Homework> homework = homeworkService.getByUserId(authUser.getId());
        model.addAttribute("homeworksByUserId", homeworkService.getByUserId(authUser.getId()));
        return "users-homeworks";
    }

    @GetMapping("/teacher/homeworks")
    public String showAllHomeworksByTeacherId(HttpSession session, Model model) {

        User authUser;
        List<Homework> homeworks;
        try {
            authUser = authenticationHelper.tryGetUser(session);
            homeworks = homeworkService.getByTeacherId(authUser);
        } catch (AuthenticationFailureException e) {
            model.addAttribute("error", e.getMessage());
            return "redirect:/auth/login";
        } catch (UnauthorizedOperationException e) {
            model.addAttribute("unauth", e.getMessage());
            return "access-denied";
        }

        model.addAttribute("homeworksByTeacherId", homeworks);
        return "users-homeworks";
    }

    @PostMapping("/homeworks/{id}/grade")
    public String updateStudentsGrade( @PathVariable int id, Model model, HttpSession session, @RequestParam("grade") double grade){

        try{
            authenticationHelper.tryGetUser(session);
        }catch (AuthenticationFailureException e){
            model.addAttribute("error",e.getMessage());
            return "redirect:/auth/login";
        }

        try {
            Homework homeworkToUpdate = homeworkService.getHomeworkById(id);
            homeworkMapper.updateHomeworkGrade(homeworkToUpdate, grade);
            homeworkService.update(homeworkToUpdate);
        }catch (EntityNotFoundException e){
            model.addAttribute("error", e.getMessage());
            return "not-found";
        }catch (IllegalArgumentException e){
            model.addAttribute("error",e.getMessage());
            return "redirect:/users/teacher/homeworks";
        }

        return "redirect:/users/teacher/homeworks";
    }

}
