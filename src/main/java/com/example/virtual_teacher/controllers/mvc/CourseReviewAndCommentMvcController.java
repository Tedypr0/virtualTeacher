package com.example.virtual_teacher.controllers.mvc;

import com.example.virtual_teacher.exceptions.AuthenticationFailureException;
import com.example.virtual_teacher.exceptions.DuplicateEntityException;
import com.example.virtual_teacher.exceptions.EntityNotFoundException;
import com.example.virtual_teacher.exceptions.UnauthorizedOperationException;
import com.example.virtual_teacher.helpers.AuthenticationHelper;
import com.example.virtual_teacher.models.Course;
import com.example.virtual_teacher.models.CourseComment;
import com.example.virtual_teacher.models.Rating;
import com.example.virtual_teacher.models.User;
import com.example.virtual_teacher.models.dtos.CourseCommentDto;
import com.example.virtual_teacher.models.dtos.RatingDto;
import com.example.virtual_teacher.services.contracts.CourseCommentService;
import com.example.virtual_teacher.services.contracts.CourseService;
import com.example.virtual_teacher.services.contracts.RatingService;
import com.example.virtual_teacher.services.mappers.CourseCommentMapper;
import com.example.virtual_teacher.services.mappers.RatingMapper;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpSession;
import javax.validation.Valid;

@Controller
@RequestMapping("/courses")
public class CourseReviewAndCommentMvcController {

    private final CourseService courseService;
    private final RatingService ratingService;
    private final CourseCommentService courseCommentService;
    private final AuthenticationHelper authenticationHelper;
    private final CourseCommentMapper courseCommentMapper;
    private final RatingMapper ratingMapper;


    public CourseReviewAndCommentMvcController(CourseService courseService, RatingService ratingService,
                                               CourseCommentService courseCommentService,
                                               AuthenticationHelper authenticationHelper,
                                               CourseCommentMapper courseCommentMapper, RatingMapper ratingMapper) {
        this.courseService = courseService;
        this.ratingService = ratingService;
        this.courseCommentService = courseCommentService;
        this.authenticationHelper = authenticationHelper;
        this.courseCommentMapper = courseCommentMapper;
        this.ratingMapper = ratingMapper;
    }

    @GetMapping("/{id}/comments")
    public String showSingleComment(@PathVariable int id,
                                    Model model) {
        try {
            Course course = courseService.getById(id);
            model.addAttribute("course", course);
            model.addAttribute("newComment", new CourseCommentDto());
            return "course-single";
        } catch (EntityNotFoundException e) {
            model.addAttribute("error", e.getMessage());
            return "not-found";
        } catch (UnauthorizedOperationException e) {
            model.addAttribute("error", e.getMessage());
            return "login";
        }
    }

    @PostMapping("/{id}/comments")
    public String createNewComment(@PathVariable int id,
                                   @Valid @ModelAttribute("newComment") CourseCommentDto courseCommentDto,
                                   BindingResult bindingResult,
                                   Model model,
                                   HttpSession session) {
        if (bindingResult.hasErrors()) {
            return "redirect:/courses/{id}";
        }
        try {
            User creator = authenticationHelper.tryGetUser(session);
            Course course = courseService.getById(id);
            CourseComment newComment = courseCommentMapper.fromDto(courseCommentDto, creator, course);
            courseCommentService.create(newComment);
            return "redirect:/courses/{id}";
        } catch (EntityNotFoundException | UnauthorizedOperationException e) {
            model.addAttribute("error", e.getMessage());
            return "not-found";
        }
    }

    @GetMapping("{courseId}/comments/{commentId}/update")
    public String showEditCommentPage(@PathVariable int courseId, @PathVariable int commentId,
                                      Model model, HttpSession session) {
        User authUser;
        try {
            authenticationHelper.tryGetUser(session);
        } catch (AuthenticationFailureException e) {
            return "redirect:/auth/login";
        }

        try {
            CourseComment courseComment = courseCommentService.getById(commentId);
            CourseCommentDto courseCommentDto = courseCommentMapper.objToDto(courseComment);
            model.addAttribute("commentId", commentId);
            model.addAttribute("comment", courseCommentDto);
            return "comment-update";
        } catch (EntityNotFoundException e) {
            model.addAttribute("error", e.getMessage());
            return "not-found";
        }
    }

    @PostMapping("{courseId}/comments/{commentId}/update")
    public String updateComment(@PathVariable int courseId,
                                @PathVariable int commentId,
                                @Valid @ModelAttribute("comment") CourseCommentDto courseCommentDto,
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
            CourseComment originalComment = courseCommentService.getById(commentId);
            CourseComment courseComment = courseCommentMapper.dtoToObjForUpdate(originalComment, courseCommentDto);
            courseCommentService.update(authUser, courseComment);

            return "redirect:/courses/{courseId}";
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

    @GetMapping("/{courseId}/comments/{commentId}/delete")
    public String deleteComment(@PathVariable int courseId, @PathVariable int commentId,
                                Model model, HttpSession session) {
        User user;
        try {
            user = authenticationHelper.tryGetUser(session);
        } catch (AuthenticationFailureException e) {
            return "redirect:/auth/login";
        }

        try {
            courseCommentService.delete(user, commentId);
            return "redirect:/courses/{courseId}";
        } catch (EntityNotFoundException e) {
            model.addAttribute("error", e.getMessage());
            return "not-found";
        } catch (UnauthorizedOperationException e) {
            model.addAttribute("error", e.getMessage());
            return "access-denied";
        }
    }

    @GetMapping("/{id}/ratings")
    public String showSingleRating(@PathVariable int id,
                                   Model model) {
        try {
            Course course = courseService.getById(id);
            model.addAttribute("course", course);
            model.addAttribute("newReview", new RatingDto());
            return "course-single";
        } catch (EntityNotFoundException e) {
            model.addAttribute("error", e.getMessage());
            return "not-found";
        } catch (UnauthorizedOperationException e) {
            model.addAttribute("error", e.getMessage());
            return "redirect:/auth/login";
        }
    }

    @PostMapping("/{id}/ratings")
    public String createNewRating(@PathVariable int id,
                                  @Valid @ModelAttribute("newReview") RatingDto ratingDto,
                                  BindingResult bindingResult,
                                  Model model,
                                  HttpSession session) {
        if (bindingResult.hasErrors()) {
            return "course-single";
        }
        try {
            User creator = authenticationHelper.tryGetUser(session);
            Course course = courseService.getById(id);
            Rating newRating = ratingMapper.fromDto(ratingDto, creator, course);
            ratingService.create(creator, newRating);
            return "redirect:/courses/{id}";
        } catch (EntityNotFoundException | UnauthorizedOperationException e) {
            model.addAttribute("error", e.getMessage());
            return "not-found";
        }
    }

    @GetMapping("/{courseId}/ratings/{ratingId}/delete")
    public String deleteRating(@PathVariable int courseId, @PathVariable int ratingId,
                                Model model, HttpSession session) {
        User user;
        try {
            user = authenticationHelper.tryGetUser(session);
        } catch (AuthenticationFailureException e) {
            return "redirect:/auth/login";
        }

        try {
            ratingService.delete(user, ratingId);
            return "redirect:/courses/{courseId}";
        } catch (EntityNotFoundException e) {
            model.addAttribute("error", e.getMessage());
            return "not-found";
        } catch (UnauthorizedOperationException e) {
            model.addAttribute("error", e.getMessage());
            return "access-denied";
        }
    }

}
