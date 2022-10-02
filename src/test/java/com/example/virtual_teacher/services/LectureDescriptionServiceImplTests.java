package com.example.virtual_teacher.services;

import com.example.virtual_teacher.Helpers;
import com.example.virtual_teacher.models.Lecture;
import com.example.virtual_teacher.models.LectureDescription;
import com.example.virtual_teacher.models.Note;
import com.example.virtual_teacher.models.User;
import com.example.virtual_teacher.repositories.contracts.LectureDescriptionRepository;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.junit.jupiter.MockitoExtension;

@ExtendWith(MockitoExtension.class)
public class LectureDescriptionServiceImplTests {
    
    @Mock
    LectureDescriptionRepository mockRepository;
    
    @InjectMocks
    LectureDescriptionServiceImpl service;

    @Test
    void getById_should_callRepository(){
        //Arrange
        LectureDescription mockLectureDescription = Helpers.createMockLectureDescription();
        Lecture mockLecture = Helpers.createMockLecture();
        Mockito.when(mockRepository.getByLectureId(mockLecture.getId())).thenReturn(mockLectureDescription.getDescription());

        // Act
        service.getById(mockLectureDescription.getId());

        // Assert
        Mockito.verify(mockRepository, Mockito.times(1))
                .getByLectureId(mockLecture.getId());
    }


    @Test
    void create_should_callRepository(){
        //Arrange
        LectureDescription mockLectureDescription = Helpers.createMockLectureDescription();
        Mockito.when(mockRepository.create(mockLectureDescription)).thenReturn(mockLectureDescription);

        // Act
        service.create(mockLectureDescription);

        // Assert
        Mockito.verify(mockRepository, Mockito.times(1))
                .create(mockLectureDescription);
    }

    @Test
    void update_should_callRepository(){
        //Arrange
        LectureDescription mockLectureDescription = Helpers.createMockLectureDescription();
        User initiator = Helpers.createMockStudent();
        Mockito.when(mockRepository.update(mockLectureDescription)).thenReturn(mockLectureDescription);

        // Act
        service.update(mockLectureDescription, initiator);

        // Assert
        Mockito.verify(mockRepository, Mockito.times(1))
                .update(mockLectureDescription);
    }

    @Test
    void delete_should_callRepository(){
        //Arrange
        LectureDescription mockLectureDescription = Helpers.createMockLectureDescription();
        Mockito.when(mockRepository.delete(mockLectureDescription)).thenReturn(mockLectureDescription);
        Mockito.when(mockRepository.getById(mockLectureDescription.getId())).thenReturn(mockLectureDescription);
        // Act
        service.delete(mockLectureDescription.getId());

        // Assert
        Mockito.verify(mockRepository, Mockito.times(1))
                .delete(mockLectureDescription);
    }
}
