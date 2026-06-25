package com.example.virtual_teacher.services;

import com.example.virtual_teacher.Helpers;
import com.example.virtual_teacher.models.Course;
import com.example.virtual_teacher.models.CourseLecture;
import com.example.virtual_teacher.models.Lecture;
import com.example.virtual_teacher.repositories.contracts.CourseLectureRepository;
import com.example.virtual_teacher.repositories.contracts.CourseRepository;
import com.example.virtual_teacher.repositories.contracts.LectureRepository;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.ArrayList;

@ExtendWith(MockitoExtension.class)
public class CourseLectureServiceImplTests {

    @Mock
    CourseLectureRepository mockRepository;
    @Mock
    LectureRepository lectureRepository;
    @Mock
    CourseRepository courseRepository;

    @InjectMocks
    CourseLectureServiceImpl service;

    @Test
    void createOrDeleteCourseLecture_should_callRepository_withDeleteMethod(){
        //Arrange
        CourseLecture mockCourseLecture = Helpers.createMockCourseLecture();
        Lecture mockLecture = Helpers.createMockLecture();
        Course mockCourse = Helpers.createMockCourse();
        mockLecture.setAddedToCourse(true);

        Mockito.when(lectureRepository.getById(mockCourseLecture.getLectureId()))
                .thenReturn(mockLecture);
        Mockito.when(courseRepository.getById(mockCourseLecture.getCourseId()))
                .thenReturn(mockCourse);

        Mockito.when(mockRepository.isAddedToCourse(mockCourseLecture.getCourseId(),mockCourseLecture.getLectureId()))
                .thenReturn(true);

        Mockito.when(mockRepository.getByCourseAndLectureId(mockCourseLecture.getCourseId(),mockCourseLecture.getLectureId()))
                        .thenReturn(mockCourseLecture);

        service.createOrDeleteCourseLecture(mockCourseLecture);

        Mockito.verify(mockRepository, Mockito.times(1)).delete(mockCourseLecture.getId());
        Mockito.verify(lectureRepository, Mockito.times(1)).update(mockLecture);
    }

    @Test
    void createOrDeleteCourseLecture_should_callRepository_withCreateMethod(){
        //Arrange
        CourseLecture mockCourseLecture = Helpers.createMockCourseLecture();
        Lecture mockLecture = Helpers.createMockLecture();
        Course mockCourse = Helpers.createMockCourse();
        mockLecture.setAddedToCourse(false);

        Mockito.when(lectureRepository.getById(mockCourseLecture.getLectureId()))
                .thenReturn(mockLecture);
        Mockito.when(courseRepository.getById(mockCourseLecture.getCourseId()))
                .thenReturn(mockCourse);

        Mockito.when(mockRepository.isAddedToCourse(mockCourseLecture.getCourseId(),mockCourseLecture.getLectureId()))
                .thenReturn(false);

        service.createOrDeleteCourseLecture(mockCourseLecture);

        Mockito.verify(mockRepository, Mockito.times(1)).create(Mockito.any(CourseLecture.class));
        Mockito.verify(lectureRepository, Mockito.times(1)).update(mockLecture);
    }

    @Test
    void isAddedToCourse_should_callRepository(){
        Course mockCourse = Helpers.createMockCourse();
        Lecture mockLecture = Helpers.createMockLecture();

        service.isAddedToCourse(mockCourse.getId(), mockLecture.getId());

        Mockito.verify(mockRepository, Mockito.times(1)).isAddedToCourse(mockCourse.getId(), mockLecture.getId());
    }

}
