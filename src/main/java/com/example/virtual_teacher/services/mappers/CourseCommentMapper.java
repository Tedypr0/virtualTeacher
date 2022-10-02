package com.example.virtual_teacher.services.mappers;

import com.example.virtual_teacher.models.Course;
import com.example.virtual_teacher.models.CourseComment;
import com.example.virtual_teacher.models.LectureComment;
import com.example.virtual_teacher.models.User;
import com.example.virtual_teacher.models.dtos.CourseCommentDto;
import com.example.virtual_teacher.models.dtos.LectureCommentDto;
import com.example.virtual_teacher.services.contracts.UserService;
import org.springframework.stereotype.Component;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;

@Component
public class CourseCommentMapper {

    private final UserService userService;

    public CourseCommentMapper(UserService userService) {
        this.userService = userService;
    }

    public CourseComment fromDto(CourseCommentDto courseCommentDto, User creator, Course course) {
        CourseComment courseComment = new CourseComment();
        courseComment.setContent(courseCommentDto.getContent());
        courseComment.setCourseId(course.getId());
        courseComment.setUser(userService.getById(creator.getId()));
        courseComment.setCreationDate(LocalDate.now());

        return courseComment;
    }

    public CourseCommentDto objToDto(CourseComment courseComment) {
        List<CourseComment> courseComments = new ArrayList<>();
        courseComments.add(courseComment);
        return listObjToDto(courseComments).get(0);
    }

    public List<CourseCommentDto> listObjToDto(List<CourseComment> courseComments){
        List<CourseCommentDto> result = new ArrayList<>();
        for(CourseComment c: courseComments) {
            CourseCommentDto courseCommentDto = new CourseCommentDto();
            courseCommentDto.setContent(c.getContent());
            result.add(courseCommentDto);
        }
        return result;
    }

    public CourseComment dtoToObjForUpdate(CourseComment originalComment, CourseCommentDto courseCommentDto) {
        originalComment.setContent(courseCommentDto.getContent());
        return originalComment;
    }
}
