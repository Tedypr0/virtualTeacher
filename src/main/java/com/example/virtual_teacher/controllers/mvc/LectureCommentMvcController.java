package com.example.virtual_teacher.controllers.mvc;

import com.example.virtual_teacher.exceptions.AuthenticationFailureException;
import com.example.virtual_teacher.exceptions.DuplicateEntityException;
import com.example.virtual_teacher.exceptions.EntityNotFoundException;
import com.example.virtual_teacher.exceptions.UnauthorizedOperationException;
import com.example.virtual_teacher.helpers.AuthenticationHelper;
import com.example.virtual_teacher.models.Course;
import com.example.virtual_teacher.models.Lecture;
import com.example.virtual_teacher.models.LectureComment;
import com.example.virtual_teacher.models.User;
import com.example.virtual_teacher.models.dtos.LectureCommentDto;
import com.example.virtual_teacher.services.AccessControlService;
import com.example.virtual_teacher.services.contracts.CourseService;
import com.example.virtual_teacher.services.contracts.LectureCommentService;
import com.example.virtual_teacher.services.contracts.LectureService;
import com.example.virtual_teacher.services.mappers.LectureCommentMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;

import jakarta.servlet.http.HttpSession;
import jakarta.validation.Valid;

@Controller
@RequestMapping("")
public class LectureCommentMvcController {

    private final LectureCommentService lectureCommentService;
    private final AuthenticationHelper authenticationHelper;
    private final AccessControlService accessControlService;
    private final LectureCommentMapper lectureCommentMapper;
    private final LectureService lectureService;
    private final CourseService courseService;

    @Autowired
    public LectureCommentMvcController(LectureCommentService lectureCommentService,
                                       AuthenticationHelper authenticationHelper,
                                       AccessControlService accessControlService,
                                       LectureCommentMapper lectureCommentMapper,
                                       LectureService lectureService,
                                       CourseService courseService) {
        this.lectureCommentService = lectureCommentService;
        this.authenticationHelper = authenticationHelper;
        this.accessControlService = accessControlService;
        this.lectureCommentMapper = lectureCommentMapper;
        this.lectureService = lectureService;
        this.courseService = courseService;
    }

    @PostMapping("/courses/{coursePublicId}/lectures/{lecturePublicId}/comments/createComment")
    public String createNewComment(@PathVariable String coursePublicId,
                                   @PathVariable String lecturePublicId,
                                   @ModelAttribute("comment") LectureCommentDto lectureCommentDto,
                                   BindingResult errors,
                                   Model model,
                                   HttpSession session) {
        if (errors.hasErrors()) {
            return "redirect:/courses/" + coursePublicId + "/lectures/" + lecturePublicId;
        }
        try {
            User authUser = authenticationHelper.tryGetUser(session);
            Course course = courseService.getByPublicId(coursePublicId);
            Lecture lecture = lectureService.getByPublicId(lecturePublicId);
            accessControlService.assertCanViewLecture(authUser, course, lecture);
            LectureComment newComment = lectureCommentMapper.dtoToObj(lectureCommentDto, lecture.getId(), authUser);
            lectureCommentService.create(newComment);
            return "redirect:/courses/" + coursePublicId + "/lectures/" + lecturePublicId;
        } catch (AuthenticationFailureException e) {
            return "redirect:/auth/login";
        } catch (EntityNotFoundException e) {
            model.addAttribute("error", e.getMessage());
            return "not-found";
        } catch (UnauthorizedOperationException e) {
            return "access-denied";
        }
    }

    @GetMapping("/courses/{coursePublicId}/lectures/{lecturePublicId}/comments/{commentPublicId}/update")
    public String showEditCommentPage(@PathVariable String coursePublicId,
                                      @PathVariable String lecturePublicId,
                                      @PathVariable String commentPublicId,
                                      Model model,
                                      HttpSession session) {
        try {
            User authUser = authenticationHelper.tryGetUser(session);
            Course course = courseService.getByPublicId(coursePublicId);
            Lecture lecture = lectureService.getByPublicId(lecturePublicId);
            LectureComment lectureComment = lectureCommentService.getByPublicId(commentPublicId);
            accessControlService.assertCanViewLectureComment(authUser, course, lecture, lectureComment);
            model.addAttribute("comment", lectureCommentMapper.objToDto(lectureComment));
            model.addAttribute("coursePublicId", coursePublicId);
            model.addAttribute("lecturePublicId", lecturePublicId);
            model.addAttribute("commentPublicId", commentPublicId);
            return "comment-update";
        } catch (AuthenticationFailureException e) {
            return "redirect:/auth/login";
        } catch (EntityNotFoundException e) {
            model.addAttribute("error", e.getMessage());
            return "not-found";
        } catch (UnauthorizedOperationException e) {
            return "access-denied";
        }
    }

    @PostMapping("/courses/{coursePublicId}/lectures/{lecturePublicId}/comments/{commentPublicId}/update")
    public String updateComment(@PathVariable String coursePublicId,
                                @PathVariable String lecturePublicId,
                                @PathVariable String commentPublicId,
                                @Valid @ModelAttribute("comment") LectureCommentDto lectureCommentDto,
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
            return "comment-update";
        }

        try {
            LectureComment originalComment = lectureCommentService.getByPublicId(commentPublicId);
            accessControlService.assertCanModifyLectureComment(authUser, originalComment);
            LectureComment lectureComment = lectureCommentMapper.dtoToObjForUpdate(originalComment, lectureCommentDto);
            lectureCommentService.update(authUser, lectureComment);
            return "redirect:/courses/" + coursePublicId + "/lectures/" + lecturePublicId;
        } catch (DuplicateEntityException e) {
            errors.rejectValue("content", "duplicate_comment", e.getMessage());
            return "comment-update";
        } catch (EntityNotFoundException e) {
            model.addAttribute("error", e.getMessage());
            return "not-found";
        } catch (UnauthorizedOperationException e) {
            return "access-denied";
        }
    }

    @GetMapping("/courses/{coursePublicId}/lectures/{lecturePublicId}/comments/{commentPublicId}/delete")
    public String deleteComment(@PathVariable String coursePublicId,
                                @PathVariable String lecturePublicId,
                                @PathVariable String commentPublicId,
                                Model model,
                                HttpSession session) {
        try {
            User user = authenticationHelper.tryGetUser(session);
            LectureComment comment = lectureCommentService.getByPublicId(commentPublicId);
            accessControlService.assertCanModifyLectureComment(user, comment);
            lectureCommentService.delete(user, comment.getId());
            return "redirect:/courses/" + coursePublicId + "/lectures/" + lecturePublicId;
        } catch (AuthenticationFailureException e) {
            return "redirect:/auth/login";
        } catch (EntityNotFoundException e) {
            model.addAttribute("error", e.getMessage());
            return "not-found";
        } catch (UnauthorizedOperationException e) {
            return "access-denied";
        }
    }
}
