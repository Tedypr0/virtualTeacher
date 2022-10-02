package com.example.virtual_teacher.services;

import com.example.virtual_teacher.Helpers;
import com.example.virtual_teacher.models.CourseComment;
import com.example.virtual_teacher.models.CourseDescription;
import com.example.virtual_teacher.models.User;
import com.example.virtual_teacher.repositories.contracts.CourseDescriptionRepository;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.ArrayList;

@ExtendWith(MockitoExtension.class)
public class CourseDescriptionServiceImplTests {
    
    @Mock
    CourseDescriptionRepository mockRepository;
    
    @InjectMocks
    CourseDescriptionServiceImpl service;

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
        CourseDescription mockCourseDescription = Helpers.createMockCourseDescription();
        Mockito.when(mockRepository.getByCourseId(mockCourseDescription.getId())).thenReturn(mockCourseDescription.getDescription());

        // Act
        service.getById(mockCourseDescription.getId());

        // Assert
        Mockito.verify(mockRepository, Mockito.times(1))
                .getByCourseId(mockCourseDescription.getId());
    }

    @Test
    void create_should_callRepository(){
        //Arrange
        CourseDescription mockCourseDescription = Helpers.createMockCourseDescription();
        Mockito.when(mockRepository.create(mockCourseDescription)).thenReturn(mockCourseDescription);

        // Act
        service.create(mockCourseDescription);

        // Assert
        Mockito.verify(mockRepository, Mockito.times(1))
                .create(mockCourseDescription);
    }

    @Test
    void update_should_callRepository(){
        //Arrange
        CourseDescription mockCourseDescription = Helpers.createMockCourseDescription();
        User initiator = Helpers.createMockStudent();

        Mockito.when(mockRepository.update(mockCourseDescription)).thenReturn(mockCourseDescription);

        // Act
        service.update(mockCourseDescription, initiator);

        // Assert
        Mockito.verify(mockRepository, Mockito.times(1))
                .update(mockCourseDescription);
    }

    @Test
    void delete_should_callRepository(){
        //Arrange
        CourseDescription mockCourseDescription = Helpers.createMockCourseDescription();
        Mockito.when(mockRepository.delete(mockCourseDescription.getId())).thenReturn(mockCourseDescription);

        // Act
        service.delete(mockCourseDescription.getId());

        // Assert
        Mockito.verify(mockRepository, Mockito.times(1))
                .delete(mockCourseDescription.getId());
    }
    
    
}
