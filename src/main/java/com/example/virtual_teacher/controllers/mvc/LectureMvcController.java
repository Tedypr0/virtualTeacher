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

import javax.servlet.http.HttpSession;
import javax.validation.Valid;
import java.io.IOException;
import java.util.List;

@Controller
@RequestMapping("")
public class LectureMvcController {

    private final AuthenticationHelper authenticationHelper;
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
    public LectureMvcController(AuthenticationHelper authenticationHelper, LectureService lectureService, VideoRepository videoRepository, UserService userService, VideoService videoService, LectureMapper lectureMapper, LectureCommentService lectureCommentService, HomeworkMapper homeworkMapper, HomeworkService homeworkService, CourseLectureService courseLectureService, CourseService courseService) {
        this.authenticationHelper = authenticationHelper;
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

    @GetMapping("courses/{id}/lectures")
    public String showAllLectures(Model model, HttpSession session, @PathVariable int id) {
        User authUser;
        try {
            authUser = authenticationHelper.tryGetUser(session);
        } catch (AuthenticationFailureException e) {
            return "redirect:/auth/login";
        }
        model.addAttribute("course", courseService.getById(id));
        model.addAttribute("lectures", lectureService.getAllByTeacherId(authUser.getId()));
        model.addAttribute("teacherApplicationsNumber", userService.getAllTeacherApplications().size());
        return "lectures";
    }

    @GetMapping("/courses/{courseId}/lectures/{id}")
    public String showSingleLecture(@PathVariable int courseId, @PathVariable int id, Model model, HttpSession session) {
        try {
            User user = authenticationHelper.tryGetUser(session);
            Lecture lecture = lectureService.getById(id);
            String video = videoRepository.getById(lecture.getVideo().getVideoId()).getVideoUrl();
            List<LectureComment> lectureComments = lectureCommentService.getAll(id);
            model.addAttribute("lecture", lecture);
            model.addAttribute("lectureId", id);
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
        }
    }

    @PostMapping("/courses/{courseId}/lectures/{id}/update/uploadFile")
    public String uploadFile(@PathVariable int courseId,
                             @PathVariable int id,
                             @RequestParam("documentFile") MultipartFile multipartFile,
                             HttpSession session) {
        User authUser;
        try {
            authUser = authenticationHelper.tryGetUser(session);
        } catch (AuthenticationFailureException e) {
            return "redirect:/auth/login";
        }
        if (!authUser.isAdmin() && authUser.getId() != lectureService.getById(id).getTeacher().getId()) {
            return "access-denied";
        }
        try {
            Lecture lecture = lectureService.getById(id);
            lectureService.saveFile(multipartFile, lecture);
        } catch (IOException e) {
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST);
        } catch (EntityNotFoundException e) {
            throw new ResponseStatusException(HttpStatus.NOT_FOUND);
        }
        return String.format("redirect:/courses/%d/lectures/%d/update", courseId, id);
    }

    @GetMapping("/courses/{courseId}/lectures/new")
    public String showNewLecturePage(@PathVariable int courseId, Model model, HttpSession session) {
        User authUser;
        try {
            authUser = authenticationHelper.tryGetUser(session);
        } catch (AuthenticationFailureException e) {
            return "redirect:/auth/login";
        }
        if (!authUser.isTeacher() && !authUser.isAdmin()) {
            return "access-denied";
        }
        model.addAttribute("lecture", new NewLectureDto());
        model.addAttribute("courseId", courseId);
        return "lecture-new";
    }

    @PostMapping("/courses/{courseId}/lectures/new")
    public String createLecture(@PathVariable int courseId,
                                @Valid @ModelAttribute("lecture") NewLectureDto newLectureDto,
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
            return "lecture-new";
        }

        try {
            Video video = new Video();
            video.setVideoUrl(newLectureDto.getVideoUrl());
            videoService.create(video);
            Lecture lecture = lectureMapper.newLectureDtoToObj(newLectureDto, authUser, video);
            lectureService.create(lecture, authUser);
            return "redirect:/courses/{courseId}/lectures";
        } catch (DuplicateEntityException e) {
            errors.rejectValue("title", "duplicate_lecture_title", e.getMessage());
            return "lecture-new";
        } catch (EntityNotFoundException e) {
            model.addAttribute("error", e.getMessage());
            return "not-found";
        }
    }

    @GetMapping("/courses/{courseId}/lectures/{id}/update")
    public String showUpdatePage(@PathVariable int courseId,
                                 @PathVariable int id,
                                 Model model,
                                 HttpSession session) {
        User authUser;
        try {
            authUser = authenticationHelper.tryGetUser(session);
        } catch (AuthenticationFailureException e) {
            return "redirect:/auth/login";
        }
        try {
            Lecture lecture = lectureService.getById(id);
            if (authUser.getId() == lecture.getTeacher().getId() || authUser.isAdmin()) {
                UpdateLectureDto updateLectureDto = lectureMapper.updateLectureObjToDto(lecture);
                model.addAttribute("lectureId", id);
                model.addAttribute("lecture", updateLectureDto);

                return "update-lecture";
            }
            return "access-denied";
        } catch (EntityNotFoundException e) {
            model.addAttribute("error", e.getMessage());
            return "not-found";
        }
    }

    @PostMapping("/courses/{courseId}/lectures/{id}/update")
    public String updateLecture(@PathVariable int courseId,
                                @PathVariable int id,
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
            return "update-lecture";
        }

        try {
            Lecture originalLecture = lectureService.getById(id);
            Video originalVideo = originalLecture.getVideo();
            if (!originalVideo.getVideoUrl().contains(updateLectureDto.getVideoUrl())) {
                originalVideo.setVideoUrl(updateLectureDto.getVideoUrl());
            }
            Lecture updatedLecture = lectureMapper.updateLectureDtoToObj(originalLecture, updateLectureDto);
            videoService.update(updatedLecture.getVideo(), authUser);
            lectureService.update(updatedLecture, authUser);
            return String.format("redirect:/courses/%d/lectures/%d", courseId, id);
        } catch (EntityNotFoundException | UnauthorizedOperationException e) {
            model.addAttribute("error", e.getMessage());
            return "access-denied";
        }
    }

    @GetMapping("/courses/{courseId}/lectures/{id}/delete")
    public String deleteLecture(@PathVariable int courseId, @PathVariable int id, Model model, HttpSession session) {
        User authUser;
        try {
            authUser = authenticationHelper.tryGetUser(session);
        } catch (AuthenticationFailureException e) {
            return "redirect:/auth/login";
        }

        try {
            lectureService.delete(authUser, id);
            return "redirect:/courses/{courseId}/lectures";
        } catch (EntityNotFoundException e) {
            model.addAttribute("error", e.getMessage());
            return "not-found";
        } catch (UnauthorizedOperationException e) {
            model.addAttribute("error", e.getMessage());
            return "access-denied";
        }
    }

    @PostMapping("/courses/{courseId}/lectures/{id}/uploadHomework")
    public String uploadHomework(@PathVariable int courseId,
                                 @PathVariable int id,
                                 @RequestParam("documentFile") MultipartFile multipartFile,
                                 HttpSession session) {
        User authUser;
        try {
            authUser = authenticationHelper.tryGetUser(session);
        } catch (AuthenticationFailureException e) {
            return "redirect:/auth/login";
        }
        try {
            Lecture lecture = lectureService.getById(id);
            String fileName = lectureService.saveHomework(multipartFile, lecture, authUser);
            Homework homework = homeworkMapper.createObjFromParams(authUser, lecture, fileName);
            homeworkService.create(homework);
        } catch (IOException e) {
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST);
        } catch (EntityNotFoundException e) {
            throw new ResponseStatusException(HttpStatus.NOT_FOUND);
        }
        return String.format("redirect:/courses/%d/lectures/%d", courseId, id);
    }

    @PostMapping("/courses/{courseId}/lectures/{id}/addToCourse")
    public String addLectureToCourse(@PathVariable int courseId,
                                     @PathVariable int id,
                                     Model model,
                                     HttpSession session) {
        try {
            User authUser = authenticationHelper.tryGetUser(session);
        } catch (AuthenticationFailureException e) {
            return "redirect:/auth/login";
        }

        CourseLecture courseLecture = new CourseLecture();
        courseLecture.setLectureId(id);
        courseLecture.setCourseId(courseId);
        courseLectureService.createOrDeleteCourseLecture(courseLecture);
        model.addAttribute("courseLecture", courseLecture);
        return "redirect:/courses/{courseId}/lectures";
    }
}
