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
import com.example.virtual_teacher.services.AccessControlService;
import com.example.virtual_teacher.services.contracts.CourseCommentService;
import com.example.virtual_teacher.services.contracts.CourseService;
import com.example.virtual_teacher.services.contracts.RatingService;
import com.example.virtual_teacher.services.mappers.CourseCommentMapper;
import com.example.virtual_teacher.services.mappers.RatingMapper;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import jakarta.servlet.http.HttpSession;
import jakarta.validation.Valid;

@Controller
@RequestMapping("/courses")
public class CourseReviewAndCommentMvcController {

    private final CourseService courseService;
    private final RatingService ratingService;
    private final CourseCommentService courseCommentService;
    private final AuthenticationHelper authenticationHelper;
    private final AccessControlService accessControlService;
    private final CourseCommentMapper courseCommentMapper;
    private final RatingMapper ratingMapper;

    public CourseReviewAndCommentMvcController(CourseService courseService, RatingService ratingService,
                                               CourseCommentService courseCommentService,
                                               AuthenticationHelper authenticationHelper,
                                               AccessControlService accessControlService,
                                               CourseCommentMapper courseCommentMapper, RatingMapper ratingMapper) {
        this.courseService = courseService;
        this.ratingService = ratingService;
        this.courseCommentService = courseCommentService;
        this.authenticationHelper = authenticationHelper;
        this.accessControlService = accessControlService;
        this.courseCommentMapper = courseCommentMapper;
        this.ratingMapper = ratingMapper;
    }

    @PostMapping("/{publicId}/comments")
    public String createNewComment(@PathVariable String publicId,
                                   @Valid @ModelAttribute("newComment") CourseCommentDto courseCommentDto,
                                   BindingResult bindingResult,
                                   Model model,
                                   HttpSession session,
                                   RedirectAttributes redirectAttributes) {
        String courseUrl = "/courses/" + publicId + "#review";
        if (bindingResult.hasErrors()) {
            redirectAttributes.addFlashAttribute("commentError", "Comment cannot be empty.");
            return "redirect:" + courseUrl;
        }
        try {
            User creator = authenticationHelper.tryGetUser(session);
            Course course = courseService.getByPublicId(publicId);
            accessControlService.assertCanViewCourse(creator, course);
            CourseComment newComment = courseCommentMapper.fromDto(courseCommentDto, creator, course);
            courseCommentService.create(newComment);
            return "redirect:" + courseUrl;
        } catch (AuthenticationFailureException e) {
            return "redirect:/auth/login";
        } catch (EntityNotFoundException e) {
            model.addAttribute("error", e.getMessage());
            return "not-found";
        } catch (UnauthorizedOperationException e) {
            return "access-denied";
        }
    }

    @GetMapping("/{coursePublicId}/comments/{commentPublicId}/update")
    public String showEditCommentPage(@PathVariable String coursePublicId,
                                      @PathVariable String commentPublicId,
                                      Model model,
                                      HttpSession session) {
        try {
            User authUser = authenticationHelper.tryGetUser(session);
            Course course = courseService.getByPublicId(coursePublicId);
            CourseComment courseComment = courseCommentService.getByPublicId(commentPublicId);
            accessControlService.assertCanViewCourseComment(authUser, course, courseComment);
            model.addAttribute("comment", courseCommentMapper.objToDto(courseComment));
            model.addAttribute("coursePublicId", coursePublicId);
            model.addAttribute("commentPublicId", commentPublicId);
            return "course-comment-update";
        } catch (AuthenticationFailureException e) {
            return "redirect:/auth/login";
        } catch (EntityNotFoundException e) {
            model.addAttribute("error", e.getMessage());
            return "not-found";
        } catch (UnauthorizedOperationException e) {
            return "access-denied";
        }
    }

    @PostMapping("/{coursePublicId}/comments/{commentPublicId}/update")
    public String updateComment(@PathVariable String coursePublicId,
                                @PathVariable String commentPublicId,
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
            model.addAttribute("coursePublicId", coursePublicId);
            model.addAttribute("commentPublicId", commentPublicId);
            return "course-comment-update";
        }

        try {
            Course course = courseService.getByPublicId(coursePublicId);
            CourseComment originalComment = courseCommentService.getByPublicId(commentPublicId);
            accessControlService.assertCanModifyCourseComment(authUser, originalComment);
            CourseComment courseComment = courseCommentMapper.dtoToObjForUpdate(originalComment, courseCommentDto);
            courseCommentService.update(authUser, courseComment);
            return "redirect:/courses/" + coursePublicId + "#review";
        } catch (DuplicateEntityException e) {
            errors.rejectValue("content", "duplicate_comment", e.getMessage());
            model.addAttribute("coursePublicId", coursePublicId);
            model.addAttribute("commentPublicId", commentPublicId);
            return "course-comment-update";
        } catch (EntityNotFoundException e) {
            model.addAttribute("error", e.getMessage());
            return "not-found";
        } catch (UnauthorizedOperationException e) {
            model.addAttribute("error", e.getMessage());
            return "access-denied";
        }
    }

    @GetMapping("/{coursePublicId}/comments/{commentPublicId}/delete")
    public String deleteComment(@PathVariable String coursePublicId,
                                @PathVariable String commentPublicId,
                                Model model,
                                HttpSession session) {
        try {
            User user = authenticationHelper.tryGetUser(session);
            Course course = courseService.getByPublicId(coursePublicId);
            CourseComment comment = courseCommentService.getByPublicId(commentPublicId);
            accessControlService.assertCanModifyCourseComment(user, comment);
            courseCommentService.delete(user, comment.getId());
            return "redirect:/courses/" + coursePublicId + "#review";
        } catch (AuthenticationFailureException e) {
            return "redirect:/auth/login";
        } catch (EntityNotFoundException e) {
            model.addAttribute("error", e.getMessage());
            return "not-found";
        } catch (UnauthorizedOperationException e) {
            return "access-denied";
        }
    }

    @PostMapping("/{publicId}/ratings")
    public String createNewRating(@PathVariable String publicId,
                                  @Valid @ModelAttribute("newReview") RatingDto ratingDto,
                                  BindingResult bindingResult,
                                  Model model,
                                  HttpSession session) {
        if (bindingResult.hasErrors()) {
            return "course-single";
        }
        try {
            User creator = authenticationHelper.tryGetUser(session);
            Course course = courseService.getByPublicId(publicId);
            accessControlService.assertCanViewCourse(creator, course);
            Rating newRating = ratingMapper.fromDto(ratingDto, creator, course);
            ratingService.create(creator, newRating);
            return "redirect:/courses/" + publicId;
        } catch (AuthenticationFailureException e) {
            return "redirect:/auth/login";
        } catch (EntityNotFoundException e) {
            model.addAttribute("error", e.getMessage());
            return "not-found";
        } catch (UnauthorizedOperationException e) {
            return "access-denied";
        }
    }

    @GetMapping("/{coursePublicId}/ratings/{ratingPublicId}/delete")
    public String deleteRating(@PathVariable String coursePublicId,
                               @PathVariable String ratingPublicId,
                               Model model,
                               HttpSession session) {
        try {
            User user = authenticationHelper.tryGetUser(session);
            Course course = courseService.getByPublicId(coursePublicId);
            accessControlService.assertCanViewCourse(user, course);
            Rating rating = ratingService.getByPublicId(ratingPublicId);
            ratingService.delete(user, rating.getCourseId());
            return "redirect:/courses/" + coursePublicId;
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
