package com.example.virtual_teacher.services.mappers;

import com.example.virtual_teacher.models.*;
import com.example.virtual_teacher.models.dtos.*;
import com.example.virtual_teacher.repositories.contracts.TopicRepository;
import com.example.virtual_teacher.services.contracts.RatingService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Optional;

@Component
public class CourseMapper {

    private final TopicRepository topicRepository;
    private final RatingService ratingService;
    @Autowired
    public CourseMapper(TopicRepository topicRepository, RatingService ratingService) {
        this.topicRepository = topicRepository;
        this.ratingService = ratingService;
    }

    public Course dtoToObj(User teacher, CourseDto courseDto) {
        Course course = new Course();
        course.setTitle(courseDto.getTitle());
        course.setTopic(courseDto.getTopic());
        course.setDescription(courseDto.getDescription());
        course.setTeacher(teacher);
        course.setLectures(new HashSet<>());
        course.setComments(new HashSet<>());
        return course;
    }

    public Course newCourseDtoToObj(NewCourseDto newCourseDto, User authUser) {
        Course course = new Course();
        course.setTitle(newCourseDto.getTitle());
        Topic topic = topicRepository.getByTopicName(newCourseDto.getTopic());
        course.setTopic(topic);
        course.setTeacher(authUser);
        course.setStartingDate(newCourseDto.getStartingDate());
        course.setEndDate(newCourseDto.getEndDate());
        course.setAvgRating(0.0);
        return course;
    }

    public UpdateCourseDto updateCourseObjToDto(Course course) {
        UpdateCourseDto updateCourseDto = new UpdateCourseDto();
        updateCourseDto.setTitle(course.getTitle());
        updateCourseDto.setDescription(course.getDescription().getDescription());
        updateCourseDto.setTopic(course.getTopic().getTopicName());
        updateCourseDto.setStartingDate(course.getStartingDate());
        updateCourseDto.setEndDate(course.getEndDate());
        return updateCourseDto;
    }

    public Course updateCourseDtoToObj(Course originalCourse, UpdateCourseDto updateCourseDto) {
        originalCourse.setTitle(updateCourseDto.getTitle());
        if (updateCourseDto.getDescription() != null && !updateCourseDto.getDescription().equals(
                "No Description")) {
            originalCourse.getDescription().setDescription(updateCourseDto.getDescription());
            originalCourse.getDescription().setCourseId(originalCourse.getId());
        }
        Topic topic = topicRepository.getByTopicName(updateCourseDto.getTopic());
        originalCourse.setTopic(topic);
        originalCourse.setStartingDate(updateCourseDto.getStartingDate());
        originalCourse.setEndDate(updateCourseDto.getEndDate());
        return originalCourse;
    }
    public FilterOptionsCourses fromDto(CourseFilterDto courseFilterDto) {

        return new FilterOptionsCourses(
                Optional.ofNullable(courseFilterDto.getTitle()),
                Optional.ofNullable(courseFilterDto.getTopicId()),
                Optional.ofNullable(courseFilterDto.getTeacherId()),
                Optional.ofNullable(courseFilterDto.getRating()),
                Optional.ofNullable(courseFilterDto.getSortBy()),
                Optional.ofNullable(courseFilterDto.getSortOrder())
        );
    }

    public CourseDtoOut objToDto(Course course) {

        List<Course> courses = new ArrayList<>();
        courses.add(course);
        return listObjToDto(courses).get(0);
    }

    public List<CourseDtoOut> listObjToDto(List<Course> courses) {
        List<CourseDtoOut> result = new ArrayList<>();
        for (Course c : courses) {
            CourseDtoOut courseDtoOut = new CourseDtoOut();
            courseDtoOut.setTitle(c.getTitle());
            courseDtoOut.setTopic(c.getTopic());
            courseDtoOut.setDescription(c.getDescription());
            courseDtoOut.setTeacher(c.getTeacher());
            courseDtoOut.setLecturesCount(c.getComments().size());
            courseDtoOut.setCommentsCount(c.getComments().size());
            result.add(courseDtoOut);
        }
        return result;
    }
}
