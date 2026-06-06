package com.example.virtual_teacher.controllers.rest;

import com.example.virtual_teacher.exceptions.EntityNotFoundException;
import com.example.virtual_teacher.exceptions.InvalidUsernameOrPasswordException;
import com.example.virtual_teacher.exceptions.UnauthorizedOperationException;
import com.example.virtual_teacher.helpers.AuthenticationHelper;
import com.example.virtual_teacher.models.Course;
import com.example.virtual_teacher.models.Lecture;
import com.example.virtual_teacher.models.User;
import com.example.virtual_teacher.models.dtos.LectureDto;
import com.example.virtual_teacher.services.AccessControlService;
import com.example.virtual_teacher.services.contracts.CourseService;
import com.example.virtual_teacher.services.contracts.LectureService;
import com.example.virtual_teacher.services.mappers.LectureMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.server.ResponseStatusException;

import jakarta.validation.Valid;
import java.util.List;

@RestController
@RequestMapping("/api/lectures")
public class LectureController {

    private final LectureService lectureService;
    private final CourseService courseService;
    private final LectureMapper lectureMapper;
    private final AuthenticationHelper authenticationHelper;
    private final AccessControlService accessControlService;

    @Autowired
    public LectureController(LectureService lectureService, CourseService courseService,
                             LectureMapper lectureMapper, AuthenticationHelper authenticationHelper,
                             AccessControlService accessControlService) {
        this.lectureService = lectureService;
        this.courseService = courseService;
        this.lectureMapper = lectureMapper;
        this.authenticationHelper = authenticationHelper;
        this.accessControlService = accessControlService;
    }

    @GetMapping
    public List<LectureDto> getAll(@RequestHeader HttpHeaders headers) {
        try {
            authenticationHelper.tryGetUser(headers);
            return lectureMapper.listObjToDto(lectureService.getAll());
        } catch (InvalidUsernameOrPasswordException | UnauthorizedOperationException e) {
            throw new ResponseStatusException(HttpStatus.UNAUTHORIZED, e.getMessage());
        }
    }

    @GetMapping("/{publicId}")
    public Lecture getByPublicId(@RequestHeader HttpHeaders headers, @PathVariable String publicId) {
        try {
            User authUser = authenticationHelper.tryGetUser(headers);
            Lecture lecture = lectureService.getByPublicId(publicId);
            Course course = lecture.getCourse();
            if (course == null) {
                throw new EntityNotFoundException("Lecture", publicId);
            }
            accessControlService.assertCanViewLecture(authUser, course, lecture);
            return lecture;
        } catch (InvalidUsernameOrPasswordException | UnauthorizedOperationException e) {
            throw new ResponseStatusException(HttpStatus.UNAUTHORIZED, e.getMessage());
        } catch (EntityNotFoundException e) {
            throw new ResponseStatusException(HttpStatus.NOT_FOUND, e.getMessage());
        }
    }

    @PostMapping
    public Lecture create(@RequestHeader HttpHeaders headers, @Valid @RequestBody LectureDto lectureDto) {
        User authUser;
        try {
            authUser = authenticationHelper.tryGetUser(headers);
            Lecture lecture = LectureMapper.dtoToObject(lectureDto);
            return lectureService.create(lecture, authUser);
        } catch (EntityNotFoundException e) {
            throw new ResponseStatusException(HttpStatus.NOT_FOUND, e.getMessage());
        } catch (InvalidUsernameOrPasswordException | UnauthorizedOperationException e) {
            throw new ResponseStatusException(HttpStatus.UNAUTHORIZED, e.getMessage());
        }
    }

    @PutMapping("/{publicId}/update")
    public LectureDto update(@RequestHeader HttpHeaders headers,
                             @PathVariable String publicId,
                             @Valid @RequestBody LectureDto lectureDto) {
        try {
            User authUser = authenticationHelper.tryGetUser(headers);
            Lecture originalLecture = lectureService.getByPublicId(publicId);
            Course course = originalLecture.getCourse();
            if (course == null) {
                throw new EntityNotFoundException("Lecture", publicId);
            }
            accessControlService.assertCanModifyLecture(authUser, course, originalLecture);
            Lecture lecture = LectureMapper.dtoToObject(lectureDto, originalLecture);
            return LectureMapper.objToDto(lectureService.update(lecture, authUser));
        } catch (EntityNotFoundException e) {
            throw new ResponseStatusException(HttpStatus.NOT_FOUND, e.getMessage());
        } catch (InvalidUsernameOrPasswordException | UnauthorizedOperationException e) {
            throw new ResponseStatusException(HttpStatus.UNAUTHORIZED, e.getMessage());
        }
    }

    @DeleteMapping("/{publicId}")
    public LectureDto delete(@RequestHeader HttpHeaders headers, @PathVariable String publicId) {
        User authUser;
        try {
            authUser = authenticationHelper.tryGetUser(headers);
            Lecture lecture = lectureService.getByPublicId(publicId);
            Course course = lecture.getCourse();
            if (course == null) {
                throw new EntityNotFoundException("Lecture", publicId);
            }
            accessControlService.assertCanModifyLecture(authUser, course, lecture);
            return lectureMapper.objToDto(lectureService.delete(authUser, lecture.getId()));
        } catch (EntityNotFoundException e) {
            throw new ResponseStatusException(HttpStatus.NOT_FOUND, e.getMessage());
        } catch (InvalidUsernameOrPasswordException | UnauthorizedOperationException e) {
            throw new ResponseStatusException(HttpStatus.UNAUTHORIZED, e.getMessage());
        }
    }
}
