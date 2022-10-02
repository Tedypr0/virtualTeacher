package com.example.virtual_teacher.services;

import com.example.virtual_teacher.Helpers;
import com.example.virtual_teacher.exceptions.DuplicateEntityException;
import com.example.virtual_teacher.exceptions.UnauthorizedOperationException;
import com.example.virtual_teacher.models.Course;
import com.example.virtual_teacher.models.Rating;
import com.example.virtual_teacher.models.User;
import com.example.virtual_teacher.models.UserRole;
import com.example.virtual_teacher.repositories.contracts.CourseRepository;
import com.example.virtual_teacher.repositories.contracts.RatingRepository;
import com.example.virtual_teacher.repositories.contracts.UserRepository;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

@ExtendWith(MockitoExtension.class)
public class RatingServiceImplTests {

    @Mock
    RatingRepository mockRepository;

    @Mock
    CourseRepository mockCourseRepository;

    @InjectMocks
    RatingServiceImpl service;

    @Test
    void getAll_should_callRepository(){
        //Arrange
        Mockito.when(mockRepository.getAll()).thenReturn(new ArrayList<>());

        // Act
        service.getAll();

        // Assert
        Mockito.verify(mockRepository, Mockito.times(1))
                .getAll();
    }

    @Test
    void getById_should_callRepository(){
        //Arrange
        Rating mockRating = Helpers.createMockRating();
        Mockito.when(mockRepository.getById(mockRating.getId())).thenReturn(mockRating);

        // Act
        service.getById(mockRating.getId());

        // Assert
        Mockito.verify(mockRepository, Mockito.times(1))
                .getById(mockRating.getId());
    }

    @Test
    void getByCourseId_should_callRepository(){
        //Arrange
        Course course = Helpers.createMockCourse();
        Mockito.when(mockRepository.getByCourseId(course.getId())).thenReturn(new ArrayList<>());

        // Act
        service.getByCourseId(course.getId());

        // Assert
        Mockito.verify(mockRepository, Mockito.times(1))
                .getByCourseId(course.getId());
    }

    @Test
    void create_should_callRepository_when_initiatorIsAdmin(){
        //Arrange
        Rating mockRating = Helpers.createMockRating();
        User initiator = Helpers.createMockAdmin();
        Mockito.when(mockRepository.create(initiator, mockRating)).thenReturn(mockRating);

        // Act
        service.create(initiator, mockRating);

        // Assert
        Mockito.verify(mockRepository, Mockito.times(1))
                .create(initiator, mockRating);
    }

    @Test
    void create_should_callRepository_when_initiatorIsTeacher(){
        //Arrange
        Rating mockRating = Helpers.createMockRating();
        User initiator = Helpers.createMockTeacher();
        Mockito.when(mockRepository.create(initiator, mockRating)).thenReturn(mockRating);

        // Act
        service.create(initiator, mockRating);

        // Assert
        Mockito.verify(mockRepository, Mockito.times(1))
                .create(initiator, mockRating);
    }

    @Test
    void create_should_throwException_when_initiatorIsNotAdminOrTeacher(){
        //Arrange
        Rating mockRating = Helpers.createMockRating();
        User initiator = Helpers.createMockStudent();

        // Assert
        Assertions.assertThrows(UnauthorizedOperationException.class,
                () -> service.create(initiator,mockRating));
    }

    @Test
    void update_should_callRepository(){
        //Arrange
        Rating mockRating = Helpers.createMockRating();
        User initiator = Helpers.createMockStudent();
        Mockito.when(mockRepository.update(initiator, mockRating)).thenReturn(mockRating);

        // Act
        service.update(initiator, mockRating);

        // Assert
        Mockito.verify(mockRepository, Mockito.times(1))
                .update(initiator, mockRating);
    }

    @Test
    void delete_should_callRepository(){
        //Arrange
        Rating mockRating = Helpers.createMockRating();
        Course course = Helpers.createMockCourse();
        User initiator = Helpers.createMockStudent();
        Mockito.when(mockRepository.delete(initiator, course.getId())).thenReturn(mockRating);

        // Act
        service.delete(initiator, course.getId());

        // Assert
        Mockito.verify(mockRepository, Mockito.times(1))
                .delete(initiator, course.getId());
    }

    @Test
    void getAvgRatingByCourseId_should_callRepository(){
        //Arrange
        Course course = Helpers.createMockCourse();

        Mockito.when(mockRepository.getAvgRatingByCourseId(course.getId())).thenReturn(1.5);

        // Act
        service.getAvgRatingByCourseId(course.getId());

        // Assert
        Mockito.verify(mockRepository, Mockito.times(1))
                .getAvgRatingByCourseId(course.getId());
    }

    @Test
    void updateAvgRatingOfCourses_should_callCourseRepository(){
        List<Course> courses = new ArrayList<>();
        courses.add(Helpers.createMockCourse());
        service.updateAvgRatingOfCourses(courses);

    }

    @Test
    void getRatingsCount_should_callRepository(){
        Course course = Helpers.createMockCourse();

        Mockito.when(mockRepository.getRatingsCount(course.getId())).thenReturn( Long.valueOf(1));

        service.getRatingsCount(course.getId());

        Mockito.verify(mockRepository, Mockito.times(1))
                .getRatingsCount(course.getId());
    }


}
