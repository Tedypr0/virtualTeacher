package com.example.virtual_teacher.controllers.mvc;

import com.example.virtual_teacher.exceptions.AuthenticationFailureException;
import com.example.virtual_teacher.exceptions.DuplicateEntityException;
import com.example.virtual_teacher.exceptions.EntityNotFoundException;
import com.example.virtual_teacher.exceptions.UnauthorizedOperationException;
import com.example.virtual_teacher.helpers.AuthenticationHelper;
import com.example.virtual_teacher.models.*;
import com.example.virtual_teacher.models.dtos.*;
import com.example.virtual_teacher.services.AccessControlService;
import com.example.virtual_teacher.services.contracts.*;
import com.example.virtual_teacher.services.mappers.CourseCommentMapper;
import com.example.virtual_teacher.services.mappers.CourseMapper;
import com.example.virtual_teacher.services.mappers.UsersCoursesMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;

import jakarta.servlet.http.HttpSession;
import jakarta.validation.Valid;
import java.sql.Date;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.List;

@Controller
@RequestMapping("/courses")
public class CourseMvcController {

    private final CourseService courseService;
    private final UserService userService;
    private final LectureService lectureService;
    private final RatingService ratingService;
    private final TopicService topicService;
    private final CourseCommentService courseCommentService;
    private final CourseMapper courseMapper;
    private final UsersCoursesMapper usersCoursesMapper;
    private final CourseCommentMapper courseCommentMapper;
    private final AuthenticationHelper authenticationHelper;
    private final AccessControlService accessControlService;

    @Autowired
    public CourseMvcController(CourseService courseService, UserService userService, LectureService lectureService,
                               RatingService ratingService, TopicService topicService,
                               CourseCommentService courseCommentService, CourseMapper courseMapper,
                               UsersCoursesMapper usersCoursesMapper, CourseCommentMapper courseCommentMapper,
                               AuthenticationHelper authenticationHelper,
                               AccessControlService accessControlService) {
        this.courseService = courseService;
        this.lectureService = lectureService;
        this.ratingService = ratingService;
        this.topicService = topicService;
        this.courseCommentService = courseCommentService;
        this.courseMapper = courseMapper;
        this.usersCoursesMapper = usersCoursesMapper;
        this.courseCommentMapper = courseCommentMapper;
        this.authenticationHelper = authenticationHelper;
        this.accessControlService = accessControlService;
        this.userService = userService;
    }

    @ModelAttribute("isAuthenticated")
    public boolean populateIsAuthenticated(HttpSession session) {
        return session.getAttribute("currentUser") != null;
    }

    @ModelAttribute("currentUser")
    public Object populateUser(HttpSession session) {
        return session.getAttribute("currentUser");
    }

    @ModelAttribute("topics")
    public List<Topic> populateTopics() {
        return topicService.getAll();
    }

    @ModelAttribute("courses")
    public List<Course> populateCourses() {
        return courseService.getAll();
    }

    @ModelAttribute("coursesCount")
    public long populateCoursesCount() {
        return courseService.courseCount();
    }

    @ModelAttribute("lectureCount")
    public long populateLectureCount() {
        return lectureService.lectureCount();
    }


    @GetMapping
    public String showCourses(@ModelAttribute("filterOptions") CourseFilterDto courseFilterDto, Model model) {
        FilterOptionsCourses filterOptionsCourses = courseMapper.fromDto(courseFilterDto);
        List<Course> courses = courseService.filter(filterOptionsCourses);
        ratingService.updateAvgRatingOfCourses(courses);

        model.addAttribute("filterOptions", courseFilterDto);
        model.addAttribute("coursesFiltered", courses);
        model.addAttribute("users", userService.getAll());
        model.addAttribute("courses", courseService.getAll());

        return "courses";
    }

    @PostMapping
    public String filterCourses(@ModelAttribute("filterOptions") CourseFilterDto courseFilterDto, Model model) {
        FilterOptionsCourses filterOptionsCourses = courseMapper.fromDto(courseFilterDto);
        List<Course> courses = courseService.filter(filterOptionsCourses);
        model.addAttribute("filterOptions", courseFilterDto);
        model.addAttribute("coursesFiltered", courses);
        model.addAttribute("users", userService.getAll());
        model.addAttribute("teacherApplicationsNumber", userService.getAllTeacherApplications().size());
        return "courses";
    }


    @GetMapping("/teacher")
    public String showSingleTeacherCourses(Model model, HttpSession session) {
        User user;
        try {
            user = authenticationHelper.tryGetUser(session);
        } catch (AuthenticationFailureException e) {
            return "redirect:/auth/login";
        }
        List<Course> courses = courseService.getByTeacherId(user.getId());

        ratingService.updateAvgRatingOfCourses(courses);

        model.addAttribute("user", user);
        model.addAttribute("singleTeacherCourses", courseService.getByTeacherId(user.getId()));

        return "courses-teacher-single";
    }

    @GetMapping("/{publicId}")
    public String showSingleCourse(@PathVariable String publicId, Model model, HttpSession session) {
        try {
            List<Course> courses = courseService.getAll();
            ratingService.updateAvgRatingOfCourses(courses);
            Course course = courseService.getByPublicId(publicId);
            User user = authenticationHelper.tryGetUser(session);
            accessControlService.assertCanViewCourse(user, course);
            if (courseService.getAll().size() < 3) {
                model.addAttribute("relatedCourses", courses);
            } else {
                courses = courses.subList(0, 3);
                model.addAttribute("relatedCourses", courses);
            }
            model.addAttribute("course", course);
            model.addAttribute("courseId", course.getId());
            model.addAttribute("coursePublicId", course.getPublicId());
            model.addAttribute("user", user);
            model.addAttribute("teacherApplicationsNumber", userService.getAllTeacherApplications().size());
            model.addAttribute("avgRating", ratingService.getAvgRatingByCourseId(course.getId()));
            model.addAttribute("newReview", new RatingDto());
            model.addAttribute("ratingsByCourseId", ratingService.getByCourseId(course.getId()));
            model.addAttribute("ratingsCount", ratingService.getRatingsCount(course.getId()));
            model.addAttribute("lectures", lectureService.getByCourseId(course.getId()));
            model.addAttribute("commentsByCourseId", courseCommentService.getByCourseId(course.getId()));
            model.addAttribute("newComment", new CourseCommentDto());
            model.addAttribute("userGrade", user);

            boolean isEnrolled = userService.isEnrolled(course.getId(), user.getId());
            Date currentDate = new Date(System.currentTimeMillis());
            model.addAttribute("isEnrolled", isEnrolled);

            if (course.getStartingDate().before(currentDate) && isEnrolled) {
                model.addAttribute("isStarted", true);
            } else {
                model.addAttribute("isStarted", false);
            }

            if (course.getEndDate().before(currentDate)) {
                model.addAttribute("isCompleted", true);
            } else {
                model.addAttribute("isCompleted", false);
            }

            return "course-single";
        } catch (AuthenticationFailureException e) {
            return "redirect:/auth/login";
        } catch (EntityNotFoundException e) {
            model.addAttribute("error", e.getMessage());
            return "not-found";
        } catch (UnauthorizedOperationException e) {
            model.addAttribute("error", e.getMessage());
            return "access-denied";
        }
    }

    @GetMapping("/new")
    public String showNewCoursePage(Model model, HttpSession session) {
        User authUser;
        try {
            authUser = authenticationHelper.tryGetUser(session);
        } catch (AuthenticationFailureException e) {
            return "redirect:/auth/login";
        }
        if (!authUser.isTeacher()) {
            return "access-denied";
        }
        model.addAttribute("course", new NewCourseDto());
        model.addAttribute("topics", populateTopics());
        return "course-new";
    }

    @PostMapping("/new")
    public String createCourse(@Valid @ModelAttribute("course") NewCourseDto newCourseDto,
                               BindingResult errors,
                               Model model,
                               HttpSession session) {
        User authUser;
        try {
            authUser = authenticationHelper.tryGetUser(session);
        } catch (AuthenticationFailureException e) {
            return "redirect:/auth/login";
        }
        if (!authUser.isTeacher() && !authUser.isAdmin()) {
            return "access-denied";
        }

        if (errors.hasErrors()) {
            return "course-new";
        }

        try {
            Course course = courseMapper.newCourseDtoToObj(newCourseDto, authUser);
            courseService.create(authUser, course);
            return "redirect:/courses/teacher";
        } catch (DuplicateEntityException e) {
            errors.rejectValue("title", "duplicate_course", e.getMessage());
            return "course-new";
        } catch (EntityNotFoundException e) {
            errors.rejectValue("topic", "invalid_topic", e.getMessage());
            return "course-new";
        } catch (UnauthorizedOperationException e) {
            model.addAttribute("error", e.getMessage());
            return "access-denied";
        }
    }

    @PostMapping("/{publicId}/enroll")
    public String enrollToCourse(@PathVariable String publicId,
                                 @Valid @ModelAttribute("usersCourses") UsersCourses usersCourses,
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
            return "course-single";
        }
        try {
            Course course = courseService.getByPublicId(publicId);
            accessControlService.assertCanEnroll(authUser, course);
            usersCourses = usersCoursesMapper.studentEnrollToCourse(course, authUser.getId());
            userService.enrollToCourse(usersCourses);
            return "redirect:/courses/" + publicId;
        } catch (EntityNotFoundException e) {
            model.addAttribute("error", e.getMessage());
            return "not-found";
        } catch (UnauthorizedOperationException e) {
            model.addAttribute("error", e.getMessage());
            return "access-denied";
        }
    }

    @PostMapping("/{publicId}/update/status")
    public String updateCourseStatus(@PathVariable String publicId,
                                     Model model,
                                     HttpSession session) {
        User authUser;
        try {
            authUser = authenticationHelper.tryGetUser(session);
        } catch (AuthenticationFailureException e) {
            return "redirect:/auth/login";
        }
        try {
            courseService.toggleDraftStatus(authUser, publicId);
            return "redirect:/courses/teacher";
        } catch (EntityNotFoundException e) {
            model.addAttribute("error", e.getMessage());
            return "not-found";
        } catch (UnauthorizedOperationException e) {
            model.addAttribute("error", e.getMessage());
            return "access-denied";
        }
    }

    @GetMapping("/{publicId}/update")
    public String showCourseUpdatePage(@PathVariable String publicId,
                                       Model model,
                                       HttpSession session) {
        User authUser;
        try {
            authUser = authenticationHelper.tryGetUser(session);
        } catch (AuthenticationFailureException e) {
            return "redirect:/auth/login";
        }

        try {
            Course course = courseService.getByPublicId(publicId);
            accessControlService.assertCanModifyCourse(authUser, course);
            UpdateCourseDto updateCourseDto = courseMapper.updateCourseObjToDto(course);
            model.addAttribute("coursePublicId", course.getPublicId());
            model.addAttribute("course", updateCourseDto);
            return "course-update";
        } catch (EntityNotFoundException e) {
            model.addAttribute("error", e.getMessage());
            return "not-found";
        } catch (UnauthorizedOperationException e) {
            model.addAttribute("error", e.getMessage());
            return "access-denied";
        }
    }

    @PostMapping("/{publicId}/update")
    public String updateCourse(@PathVariable String publicId,
                               @Valid @ModelAttribute("course") UpdateCourseDto updateCourseDto,
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
            return "course-update";
        }

        try {
            Course originalCourse = courseService.getByPublicId(publicId);
            accessControlService.assertCanModifyCourse(authUser, originalCourse);
            Course updatedCourse = courseMapper.updateCourseDtoToObj(originalCourse, updateCourseDto);
            courseService.update(authUser, updatedCourse);
            return "redirect:/courses/teacher";
        } catch (EntityNotFoundException e) {
            model.addAttribute("error", e.getMessage());
            return "not-found";
        } catch (UnauthorizedOperationException e) {
            model.addAttribute("error", e.getMessage());
            return "access-denied";
        }
    }

    @GetMapping("/{publicId}/delete")
    public String deleteCourse(@PathVariable String publicId, Model model, HttpSession session) {
        User authUser;
        try {
            authUser = authenticationHelper.tryGetUser(session);
        } catch (AuthenticationFailureException e) {
            return "redirect:/auth/login";
        }

        try {
            Course course = courseService.getByPublicId(publicId);
            courseService.delete(authUser, course.getId());
            return "redirect:/courses/teacher";
        } catch (EntityNotFoundException e) {
            model.addAttribute("error", e.getMessage());
            return "not-found";
        } catch (UnauthorizedOperationException e) {
            model.addAttribute("error", e.getMessage());
            return "access-denied";
        }
    }
}
