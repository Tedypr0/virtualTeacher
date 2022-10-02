package com.example.virtual_teacher.services;

import com.example.virtual_teacher.Helpers;
import com.example.virtual_teacher.exceptions.UnauthorizedOperationException;
import com.example.virtual_teacher.models.Course;
import com.example.virtual_teacher.models.CourseComment;
import com.example.virtual_teacher.models.User;
import com.example.virtual_teacher.models.Video;
import com.example.virtual_teacher.repositories.CourseCommentRepositoryImpl;
import com.example.virtual_teacher.repositories.contracts.CourseCommentRepository;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.ArrayList;

@ExtendWith(MockitoExtension.class)
public class CourseCommentServiceImplTests {

    @Mock
    CourseCommentRepository mockRepository;

    @InjectMocks
    CourseCommentServiceImpl service;


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
        CourseComment mockCourseComment = Helpers.createMockCourseComment();
        Mockito.when(mockRepository.getById(mockCourseComment.getId())).thenReturn(mockCourseComment);

        // Act
        service.getById(mockCourseComment.getId());

        // Assert
        Mockito.verify(mockRepository, Mockito.times(1))
                .getById(mockCourseComment.getId());
    }

    @Test
    void create_should_callRepository(){
        //Arrange
        CourseComment mockCourseComment = Helpers.createMockCourseComment();
        Mockito.when(mockRepository.create(mockCourseComment)).thenReturn(mockCourseComment);

        // Act
        service.create(mockCourseComment);

        // Assert
        Mockito.verify(mockRepository, Mockito.times(1))
                .create(mockCourseComment);
    }

    @Test
    void update_should_callRepository_whenInitiatorIsOwner(){
        //Arrange
        CourseComment mockCourseComment = Helpers.createMockCourseComment();
        User initiator = Helpers.createMockTeacher();
        Mockito.when(mockRepository.update(mockCourseComment)).thenReturn(mockCourseComment);

        // Act
        service.update(initiator, mockCourseComment);

        // Assert
        Mockito.verify(mockRepository, Mockito.times(1))
                .update(mockCourseComment);
    }

    @Test
    void update_should_callRepository_whenInitiatorIsAdmin(){
        //Arrange
        CourseComment mockCourseComment = Helpers.createMockCourseComment();
        User initiator = Helpers.createMockAdmin();
        Mockito.when(mockRepository.update(mockCourseComment)).thenReturn(mockCourseComment);

        // Act
        service.update(initiator, mockCourseComment);

        // Assert
        Mockito.verify(mockRepository, Mockito.times(1))
                .update(mockCourseComment);
    }

    @Test
    void update_should_throwException_whenInitiatorIsNotAdmin(){
        //Arrange
        CourseComment mockCourseComment = Helpers.createMockCourseComment();
        User initiator = Helpers.createMockStudent();
        initiator.setId(15);

        // Act, Assert
        Assertions.assertThrows(UnauthorizedOperationException.class,
                () -> service.update(initiator, mockCourseComment));

    }

    @Test
    void delete_should_throwException_whenInitiatorIsNotAdmin(){
        //Arrange
        CourseComment mockCourseComment = Helpers.createMockCourseComment();
        User initiator = Helpers.createMockStudent();
        initiator.setId(15);

        //Act
        Mockito.when(mockRepository.getById(mockCourseComment.getId())).thenReturn(mockCourseComment);

        //Assert
        Assertions.assertThrows(UnauthorizedOperationException.class,
                () -> service.delete(initiator, mockCourseComment.getId()));

    }

    @Test
    void delete_should_callRepository_whenInitiatorIsAdmin(){
        //Arrange
        CourseComment mockCourseComment = Helpers.createMockCourseComment();
        User initiator = Helpers.createMockAdmin();
        initiator.setId(15);

        //Act
        service.delete(initiator, mockCourseComment.getId());

        //Assert

        Mockito.verify(mockRepository, Mockito.times(1))
                .delete(mockCourseComment.getId());

    }

    @Test
    void getByCourseId_should_callRepository(){
        Course mockCourse = Helpers.createMockCourse();

        Mockito.when(mockRepository.getByCourseId(mockCourse.getId())).thenReturn(new ArrayList<>());

        service.getByCourseId(mockCourse.getId());

        Mockito.verify(mockRepository, Mockito.times(1))
                .getByCourseId(mockCourse.getId());
    }

    @Test
    void getCommentsCount_should_callRepository(){

        Mockito.when(mockRepository.getCourseCommentsCount()).thenReturn(Long.valueOf(1));

        service.getCommentsCount();

        Mockito.verify(mockRepository, Mockito.times(1))
                .getCourseCommentsCount();
    }

}
