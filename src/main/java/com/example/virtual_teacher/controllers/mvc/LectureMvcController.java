package com.example.virtual_teacher.controllers.mvc;

import com.example.virtual_teacher.exceptions.AuthenticationFailureException;
import com.example.virtual_teacher.exceptions.DuplicateEntityException;
import com.example.virtual_teacher.exceptions.EntityNotFoundException;
import com.example.virtual_teacher.exceptions.UnauthorizedOperationException;
import com.example.virtual_teacher.helpers.AuthenticationHelper;
import com.example.virtual_teacher.models.*;
import com.example.virtual_teacher.models.dtos.LectureCommentDto;
import com.example.virtual_teacher.models.dtos.NewLectureDto;
import com.example.virtual_teacher.models.dtos.NoteDto;
import com.example.virtual_teacher.models.dtos.UpdateLectureDto;
import com.example.virtual_teacher.repositories.contracts.VideoRepository;
import com.example.virtual_teacher.services.AccessControlService;
import com.example.virtual_teacher.services.contracts.*;
import com.example.virtual_teacher.services.mappers.HomeworkMapper;
import com.example.virtual_teacher.services.mappers.LectureMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.web.server.ResponseStatusException;

import jakarta.servlet.http.HttpSession;
import jakarta.validation.Valid;
import java.io.IOException;
import java.util.List;

@Controller
@RequestMapping("")
public class LectureMvcController {

    private final AuthenticationHelper authenticationHelper;
    private final AccessControlService accessControlService;
    private final LectureService lectureService;
    private final VideoRepository videoRepository;
    private final UserService userService;
    private final VideoService videoService;
    private final LectureMapper lectureMapper;
    private final LectureCommentService lectureCommentService;
    private final HomeworkMapper homeworkMapper;
    private final HomeworkService homeworkService;
    private final CourseLectureService courseLectureService;
    private final CourseService courseService;

    @Autowired
    public LectureMvcController(AuthenticationHelper authenticationHelper,
                                AccessControlService accessControlService,
                                LectureService lectureService,
                                VideoRepository videoRepository,
                                UserService userService,
                                VideoService videoService,
                                LectureMapper lectureMapper,
                                LectureCommentService lectureCommentService,
                                HomeworkMapper homeworkMapper,
                                HomeworkService homeworkService,
                                CourseLectureService courseLectureService,
                                CourseService courseService) {
        this.authenticationHelper = authenticationHelper;
        this.accessControlService = accessControlService;
        this.lectureService = lectureService;
        this.videoRepository = videoRepository;
        this.userService = userService;
        this.videoService = videoService;
        this.lectureMapper = lectureMapper;
        this.lectureCommentService = lectureCommentService;
        this.homeworkMapper = homeworkMapper;
        this.homeworkService = homeworkService;
        this.courseLectureService = courseLectureService;
        this.courseService = courseService;
    }

    @ModelAttribute("isAuthenticated")
    public boolean populateIsAuthenticated(HttpSession session) {
        return session.getAttribute("currentUser") != null;
    }

    @ModelAttribute("currentUser")
    public Object populateUser(HttpSession session) {
        return session.getAttribute("currentUser");
    }

    @GetMapping("courses/{coursePublicId}/lectures")
    public String showAllLectures(Model model, HttpSession session, @PathVariable String coursePublicId) {
        User authUser;
        try {
            authUser = authenticationHelper.tryGetUser(session);
            Course course = courseService.getByPublicId(coursePublicId);
            accessControlService.assertCanViewCourse(authUser, course);
            model.addAttribute("course", course);
            model.addAttribute("coursePublicId", course.getPublicId());
            model.addAttribute("lectures", lectureService.getAllByTeacherId(authUser.getId()));
            model.addAttribute("teacherApplicationsNumber", userService.getAllTeacherApplications().size());
            return "lectures";
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

    @GetMapping("/courses/{coursePublicId}/lectures/{lecturePublicId}")
    public String showSingleLecture(@PathVariable String coursePublicId,
                                    @PathVariable String lecturePublicId,
                                    Model model,
                                    HttpSession session) {
        try {
            User user = authenticationHelper.tryGetUser(session);
            Course course = courseService.getByPublicId(coursePublicId);
            Lecture lecture = lectureService.getByPublicId(lecturePublicId);
            accessControlService.assertCanViewLecture(user, course, lecture);
            String video = videoRepository.getById(lecture.getVideo().getVideoId()).getVideoUrl();
            List<LectureComment> lectureComments = lectureCommentService.getAll(lecture.getId());
            model.addAttribute("course", course);
            model.addAttribute("coursePublicId", course.getPublicId());
            model.addAttribute("lecture", lecture);
            model.addAttribute("lecturePublicId", lecture.getPublicId());
            model.addAttribute("lectureId", lecture.getId());
            model.addAttribute("video", video);
            model.addAttribute("newComment", new LectureCommentDto());
            model.addAttribute("comments", lectureComments);
            model.addAttribute("user", user);
            model.addAttribute("note", new NoteDto());
            return "lecture-single";
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

    @PostMapping("/courses/{coursePublicId}/lectures/{lecturePublicId}/update/uploadFile")
    public String uploadFile(@PathVariable String coursePublicId,
                             @PathVariable String lecturePublicId,
                             @RequestParam("documentFile") MultipartFile multipartFile,
                             HttpSession session) {
        User authUser;
        try {
            authUser = authenticationHelper.tryGetUser(session);
            Course course = courseService.getByPublicId(coursePublicId);
            Lecture lecture = lectureService.getByPublicId(lecturePublicId);
            accessControlService.assertCanModifyLecture(authUser, course, lecture);
            lectureService.saveFile(multipartFile, lecture);
            return "redirect:/courses/" + coursePublicId + "/lectures/" + lecturePublicId + "/update";
        } catch (AuthenticationFailureException e) {
            return "redirect:/auth/login";
        } catch (IOException e) {
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST);
        } catch (EntityNotFoundException e) {
            throw new ResponseStatusException(HttpStatus.NOT_FOUND);
        } catch (UnauthorizedOperationException e) {
            return "access-denied";
        }
    }

    @GetMapping("/courses/{coursePublicId}/lectures/new")
    public String showNewLecturePage(@PathVariable String coursePublicId, Model model, HttpSession session) {
        User authUser;
        try {
            authUser = authenticationHelper.tryGetUser(session);
            Course course = courseService.getByPublicId(coursePublicId);
            accessControlService.assertCanModifyCourse(authUser, course);
            model.addAttribute("lecture", new NewLectureDto());
            model.addAttribute("coursePublicId", course.getPublicId());
            return "lecture-new";
        } catch (AuthenticationFailureException e) {
            return "redirect:/auth/login";
        } catch (EntityNotFoundException e) {
            model.addAttribute("error", e.getMessage());
            return "not-found";
        } catch (UnauthorizedOperationException e) {
            return "access-denied";
        }
    }

    @PostMapping("/courses/{coursePublicId}/lectures/new")
    public String createLecture(@PathVariable String coursePublicId,
                                @Valid @ModelAttribute("lecture") NewLectureDto newLectureDto,
                                BindingResult errors,
                                Model model,
                                HttpSession session) {
        User authUser;
        try {
            authUser = authenticationHelper.tryGetUser(session);
            Course course = courseService.getByPublicId(coursePublicId);
            accessControlService.assertCanModifyCourse(authUser, course);
        } catch (AuthenticationFailureException e) {
            return "redirect:/auth/login";
        } catch (EntityNotFoundException e) {
            model.addAttribute("error", e.getMessage());
            return "not-found";
        } catch (UnauthorizedOperationException e) {
            return "access-denied";
        }

        if (errors.hasErrors()) {
            model.addAttribute("coursePublicId", coursePublicId);
            return "lecture-new";
        }

        try {
            Video video = new Video();
            video.setVideoUrl(newLectureDto.getVideoUrl());
            videoService.create(video);
            Lecture lecture = lectureMapper.newLectureDtoToObj(newLectureDto, authUser, video);
            lectureService.create(lecture, authUser);
            return "redirect:/courses/" + coursePublicId + "/lectures";
        } catch (DuplicateEntityException e) {
            errors.rejectValue("title", "duplicate_lecture_title", e.getMessage());
            model.addAttribute("coursePublicId", coursePublicId);
            return "lecture-new";
        } catch (EntityNotFoundException e) {
            model.addAttribute("error", e.getMessage());
            return "not-found";
        }
    }

    @GetMapping("/courses/{coursePublicId}/lectures/{lecturePublicId}/update")
    public String showUpdatePage(@PathVariable String coursePublicId,
                                 @PathVariable String lecturePublicId,
                                 Model model,
                                 HttpSession session) {
        User authUser;
        try {
            authUser = authenticationHelper.tryGetUser(session);
            Course course = courseService.getByPublicId(coursePublicId);
            Lecture lecture = lectureService.getByPublicId(lecturePublicId);
            accessControlService.assertCanModifyLecture(authUser, course, lecture);
            UpdateLectureDto updateLectureDto = lectureMapper.updateLectureObjToDto(lecture);
            model.addAttribute("coursePublicId", course.getPublicId());
            model.addAttribute("lecturePublicId", lecture.getPublicId());
            model.addAttribute("lecture", updateLectureDto);
            return "update-lecture";
        } catch (AuthenticationFailureException e) {
            return "redirect:/auth/login";
        } catch (EntityNotFoundException e) {
            model.addAttribute("error", e.getMessage());
            return "not-found";
        } catch (UnauthorizedOperationException e) {
            return "access-denied";
        }
    }

    @PostMapping("/courses/{coursePublicId}/lectures/{lecturePublicId}/update")
    public String updateLecture(@PathVariable String coursePublicId,
                                @PathVariable String lecturePublicId,
                                @Valid @ModelAttribute("lecture") UpdateLectureDto updateLectureDto,
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
            model.addAttribute("lecturePublicId", lecturePublicId);
            return "update-lecture";
        }

        try {
            Course course = courseService.getByPublicId(coursePublicId);
            Lecture originalLecture = lectureService.getByPublicId(lecturePublicId);
            accessControlService.assertCanModifyLecture(authUser, course, originalLecture);
            Video originalVideo = originalLecture.getVideo();
            if (!originalVideo.getVideoUrl().contains(updateLectureDto.getVideoUrl())) {
                originalVideo.setVideoUrl(updateLectureDto.getVideoUrl());
            }
            Lecture updatedLecture = lectureMapper.updateLectureDtoToObj(originalLecture, updateLectureDto);
            videoService.update(updatedLecture.getVideo(), authUser);
            lectureService.update(updatedLecture, authUser);
            return "redirect:/courses/" + coursePublicId + "/lectures/" + lecturePublicId;
        } catch (EntityNotFoundException e) {
            model.addAttribute("error", e.getMessage());
            return "not-found";
        } catch (UnauthorizedOperationException e) {
            model.addAttribute("error", e.getMessage());
            return "access-denied";
        }
    }

    @GetMapping("/courses/{coursePublicId}/lectures/{lecturePublicId}/delete")
    public String deleteLecture(@PathVariable String coursePublicId,
                                @PathVariable String lecturePublicId,
                                Model model,
                                HttpSession session) {
        User authUser;
        try {
            authUser = authenticationHelper.tryGetUser(session);
            Course course = courseService.getByPublicId(coursePublicId);
            Lecture lecture = lectureService.getByPublicId(lecturePublicId);
            accessControlService.assertCanModifyLecture(authUser, course, lecture);
            lectureService.delete(authUser, lecture.getId());
            return "redirect:/courses/" + coursePublicId + "/lectures";
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

    @PostMapping("/courses/{coursePublicId}/lectures/{lecturePublicId}/uploadHomework")
    public String uploadHomework(@PathVariable String coursePublicId,
                                 @PathVariable String lecturePublicId,
                                 @RequestParam("documentFile") MultipartFile multipartFile,
                                 HttpSession session) {
        User authUser;
        try {
            authUser = authenticationHelper.tryGetUser(session);
            Course course = courseService.getByPublicId(coursePublicId);
            Lecture lecture = lectureService.getByPublicId(lecturePublicId);
            accessControlService.assertCanViewLecture(authUser, course, lecture);
            String fileName = lectureService.saveHomework(multipartFile, lecture, authUser);
            Homework homework = homeworkMapper.createObjFromParams(authUser, lecture, fileName);
            homeworkService.create(homework);
            return "redirect:/courses/" + coursePublicId + "/lectures/" + lecturePublicId;
        } catch (AuthenticationFailureException e) {
            return "redirect:/auth/login";
        } catch (IOException e) {
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST);
        } catch (EntityNotFoundException e) {
            throw new ResponseStatusException(HttpStatus.NOT_FOUND);
        } catch (UnauthorizedOperationException e) {
            return "access-denied";
        }
    }

    @PostMapping("/courses/{coursePublicId}/lectures/{lecturePublicId}/addToCourse")
    public String addLectureToCourse(@PathVariable String coursePublicId,
                                     @PathVariable String lecturePublicId,
                                     Model model,
                                     HttpSession session) {
        try {
            User authUser = authenticationHelper.tryGetUser(session);
            Course course = courseService.getByPublicId(coursePublicId);
            Lecture lecture = lectureService.getByPublicId(lecturePublicId);
            accessControlService.assertCanModifyLecture(authUser, course, lecture);
            CourseLecture courseLecture = new CourseLecture();
            courseLecture.setLectureId(lecture.getId());
            courseLecture.setCourseId(course.getId());
            courseLectureService.createOrDeleteCourseLecture(courseLecture);
            model.addAttribute("courseLecture", courseLecture);
            return "redirect:/courses/" + coursePublicId + "/lectures";
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
