package com.example.virtual_teacher.controllers.mvc;

import com.example.virtual_teacher.exceptions.AuthenticationFailureException;
import com.example.virtual_teacher.exceptions.DuplicateEntityException;
import com.example.virtual_teacher.exceptions.EntityNotFoundException;
import com.example.virtual_teacher.exceptions.UnauthorizedOperationException;
import com.example.virtual_teacher.helpers.AuthenticationHelper;
import com.example.virtual_teacher.models.LectureComment;
import com.example.virtual_teacher.models.User;
import com.example.virtual_teacher.models.dtos.LectureCommentDto;
import com.example.virtual_teacher.services.contracts.LectureCommentService;
import com.example.virtual_teacher.services.mappers.LectureCommentMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpSession;
import javax.validation.Valid;

@Controller
@RequestMapping("")
public class LectureCommentMvcController {

    private final LectureCommentService lectureCommentService;

    private final AuthenticationHelper authenticationHelper;

    private final LectureCommentMapper lectureCommentMapper;

    @Autowired
    public LectureCommentMvcController(LectureCommentService lectureCommentService, AuthenticationHelper authenticationHelper, LectureCommentMapper lectureCommentMapper) {
        this.lectureCommentService = lectureCommentService;
        this.authenticationHelper = authenticationHelper;
        this.lectureCommentMapper = lectureCommentMapper;
    }

    @PostMapping("/courses/{courseId}/lectures/{lectureId}/comments/createComment")
    public String createNewComment(@PathVariable int courseId,
                                   @PathVariable int lectureId,
                                   @ModelAttribute("comment") LectureCommentDto lectureCommentDto,
                                   BindingResult errors,
                                   Model model,
                                   HttpSession session) {

        if (errors.hasErrors()) {
            return String.format("redirect:/courses/%d/lectures/%d", courseId, lectureId);
        }
        User authUser;
        try {
            authUser = authenticationHelper.tryGetUser(session);
            LectureComment newComment = lectureCommentMapper.dtoToObj(lectureCommentDto, lectureId, authUser);
            lectureCommentService.create(newComment);
            return String.format("redirect:/courses/%d/lectures/%d", courseId, lectureId);
        } catch (EntityNotFoundException | UnauthorizedOperationException e) {
            model.addAttribute("error", e.getMessage());
            return "not-found";
        }
    }

    @GetMapping("/courses/{courseId}/lectures/{lectureId}/comments/{commentId}/update")
    public String showEditCommentPage(@PathVariable int courseId,
                                      @PathVariable int lectureId,
                                      @PathVariable int commentId,
                                      Model model, HttpSession session) {
        User authUser;
        try {
            authUser = authenticationHelper.tryGetUser(session);
        } catch (AuthenticationFailureException e) {
            return "redirect:/auth/login";
        }

        try {
            LectureComment lectureComment = lectureCommentService.getById(commentId);
            LectureCommentDto lectureCommentDto = lectureCommentMapper.objToDto(lectureComment);
            model.addAttribute("commentId", commentId);
            model.addAttribute("comment", lectureCommentDto);
            return "comment-update";
        } catch (EntityNotFoundException e) {
            model.addAttribute("error", e.getMessage());
            return "not-found";
        }
    }

    @PostMapping("/courses/{courseId}/lectures/{lectureId}/comments/{commentId}/update")
    public String updateComment(@PathVariable int courseId,
                                @PathVariable int lectureId,
                                @PathVariable int commentId,
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
            LectureComment originalComment = lectureCommentService.getById(commentId);
            LectureComment lectureComment = lectureCommentMapper.dtoToObjForUpdate(originalComment, lectureCommentDto);
            lectureCommentService.update(authUser, lectureComment);

            return "redirect:/courses/{courseId}/lectures/{lectureId}";
        } catch (DuplicateEntityException e) {
            errors.rejectValue("content", "duplicate_comment", e.getMessage());
            return "comment-update";
        } catch (EntityNotFoundException e) {
            model.addAttribute("error", e.getMessage());
            return "not-found";
        } catch (UnauthorizedOperationException e) {
            model.addAttribute("error", e.getMessage());
            return "access-denied";
        }
    }

    @GetMapping("/courses/{courseId}/lectures/{lectureId}/comments/{commentId}/delete")
    public String deleteComment(@PathVariable int courseId,
                                @PathVariable int lectureId,
                                @PathVariable int commentId,
                                Model model, HttpSession session) {
        User user;
        try {
            user = authenticationHelper.tryGetUser(session);
        } catch (AuthenticationFailureException e) {
            return "redirect:/auth/login";
        }

        try {
            lectureCommentService.delete(user, commentId);
            return "redirect:/courses/{courseId}/lectures/{lectureId}";
        } catch (EntityNotFoundException e) {
            model.addAttribute("error", e.getMessage());
            return "not-found";
        } catch (UnauthorizedOperationException e) {
            model.addAttribute("error", e.getMessage());
            return "access-denied";
        }
    }
}
