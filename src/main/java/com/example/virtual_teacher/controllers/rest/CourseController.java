package com.example.virtual_teacher.controllers.rest;

import com.example.virtual_teacher.exceptions.EntityNotFoundException;
import com.example.virtual_teacher.exceptions.InvalidUsernameOrPasswordException;
import com.example.virtual_teacher.exceptions.UnauthorizedOperationException;
import com.example.virtual_teacher.helpers.AuthenticationHelper;
import com.example.virtual_teacher.models.Course;
import com.example.virtual_teacher.models.User;
import com.example.virtual_teacher.models.dtos.CourseDto;
import com.example.virtual_teacher.models.dtos.CourseDtoOut;
import com.example.virtual_teacher.services.AccessControlService;
import com.example.virtual_teacher.services.contracts.CourseService;
import com.example.virtual_teacher.services.mappers.CourseMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.server.ResponseStatusException;

import jakarta.validation.Valid;
import java.util.List;

@RestController
@RequestMapping("/api/courses")
public class CourseController {
    private final CourseService courseService;
    private final CourseMapper courseMapper;
    private final AuthenticationHelper authenticationHelper;
    private final AccessControlService accessControlService;

    @Autowired
    public CourseController(CourseService courseService, CourseMapper courseMapper,
                            AuthenticationHelper authenticationHelper,
                            AccessControlService accessControlService) {
        this.courseService = courseService;
        this.courseMapper = courseMapper;
        this.authenticationHelper = authenticationHelper;
        this.accessControlService = accessControlService;
    }

    @GetMapping
    public List<CourseDtoOut> getAll() {
        return courseMapper.listObjToDto(courseService.getAll());
    }


    @GetMapping("/{publicId}")
    public CourseDtoOut getByPublicId(@PathVariable String publicId, @RequestHeader HttpHeaders headers) {
        try {
            User authUser = authenticationHelper.tryGetUser(headers);
            Course course = courseService.getByPublicId(publicId);
            accessControlService.assertCanViewCourse(authUser, course);
            return courseMapper.objToDto(course);
        } catch (InvalidUsernameOrPasswordException | UnauthorizedOperationException e) {
            throw new ResponseStatusException(HttpStatus.UNAUTHORIZED, e.getMessage());
        } catch (EntityNotFoundException e) {
            throw new ResponseStatusException(HttpStatus.NOT_FOUND, e.getMessage());
        }
    }

    @PostMapping
    public Course create(@RequestHeader HttpHeaders headers, @Valid @RequestBody CourseDto courseDto) {
        try {
            User authUser = authenticationHelper.tryGetUser(headers);
            Course course = courseMapper.dtoToObj(authUser ,courseDto);
            return courseService.create(authUser, course);
        } catch (InvalidUsernameOrPasswordException | UnauthorizedOperationException e) {
            throw new ResponseStatusException(HttpStatus.UNAUTHORIZED, e.getMessage());
        }
    }

    @PutMapping("/{publicId}")
    public Course update(@PathVariable String publicId, @RequestHeader HttpHeaders headers) {
        try {
            User authUser = authenticationHelper.tryGetUser(headers);
            Course courseToUpdate = courseService.getByPublicId(publicId);
            accessControlService.assertCanModifyCourse(authUser, courseToUpdate);
            return courseService.update(authUser, courseToUpdate);
        } catch (EntityNotFoundException e) {
            throw new ResponseStatusException(HttpStatus.NOT_FOUND, e.getMessage());
        } catch (InvalidUsernameOrPasswordException | UnauthorizedOperationException e) {
            throw new ResponseStatusException(HttpStatus.UNAUTHORIZED, e.getMessage());
        }
    }

    @DeleteMapping("/{publicId}")
    public Course delete(@PathVariable String publicId, @RequestHeader HttpHeaders headers) {
        try {
            User authUser = authenticationHelper.tryGetUser(headers);
            Course course = courseService.getByPublicId(publicId);
            accessControlService.assertCanModifyCourse(authUser, course);
            return courseService.delete(authUser, course.getId());
        } catch (EntityNotFoundException e) {
            throw new ResponseStatusException(HttpStatus.NOT_FOUND, e.getMessage());
        } catch (InvalidUsernameOrPasswordException | UnauthorizedOperationException e) {
            throw new ResponseStatusException(HttpStatus.UNAUTHORIZED, e.getMessage());
        }
    }
}
