package com.example.virtual_teacher.controllers.rest;

import com.example.virtual_teacher.exceptions.EntityNotFoundException;
import com.example.virtual_teacher.exceptions.InvalidUsernameOrPasswordException;
import com.example.virtual_teacher.exceptions.UnauthorizedOperationException;
import com.example.virtual_teacher.helpers.AuthenticationHelper;
import com.example.virtual_teacher.models.Lecture;
import com.example.virtual_teacher.models.User;
import com.example.virtual_teacher.models.dtos.LectureDto;
import com.example.virtual_teacher.services.contracts.LectureService;
import com.example.virtual_teacher.services.mappers.LectureMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.server.ResponseStatusException;

import javax.validation.Valid;
import java.util.List;

@RestController
@RequestMapping("/api/lectures")
public class LectureController {

    private final LectureService lectureService;

    private final LectureMapper lectureMapper;

    private final AuthenticationHelper authenticationHelper;

    @Autowired
    public LectureController(LectureService lectureService, LectureMapper lectureMapper, AuthenticationHelper authenticationHelper) {
        this.lectureService = lectureService;
        this.lectureMapper = lectureMapper;
        this.authenticationHelper = authenticationHelper;
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

    @GetMapping("/{id}")
    public Lecture getById(@RequestHeader HttpHeaders headers, @PathVariable @Valid int id) {
        try {
            authenticationHelper.tryGetUser(headers);
            return lectureService.getById(id);
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

    @PutMapping("/{id}/update")
    public LectureDto update(@RequestHeader HttpHeaders headers,
                             @PathVariable int id,
                             @Valid @RequestBody LectureDto lectureDto) {
        try {
            User authUser = authenticationHelper.tryGetUser(headers);
            Lecture originalLecture = lectureService.getById(id);
            Lecture lecture = LectureMapper.dtoToObject(lectureDto, originalLecture);
            return LectureMapper.objToDto(lectureService.update(lecture, authUser));
        } catch (EntityNotFoundException e) {
            throw new ResponseStatusException(HttpStatus.NOT_FOUND, e.getMessage());
        } catch (InvalidUsernameOrPasswordException | UnauthorizedOperationException e) {
            throw new ResponseStatusException(HttpStatus.UNAUTHORIZED, e.getMessage());
        }
    }

    @DeleteMapping("/{id}")
    public LectureDto delete(@RequestHeader HttpHeaders headers, @PathVariable int id) {
        User authUser;
        try {
            authUser = authenticationHelper.tryGetUser(headers);
            return lectureMapper.objToDto(lectureService.delete(authUser, id));
        } catch (EntityNotFoundException e) {
            throw new ResponseStatusException(HttpStatus.NOT_FOUND, e.getMessage());
        } catch (InvalidUsernameOrPasswordException | UnauthorizedOperationException e) {
            throw new ResponseStatusException(HttpStatus.UNAUTHORIZED, e.getMessage());
        }
    }
}
