package com.example.virtual_teacher.services;

import com.example.virtual_teacher.Helpers;
import com.example.virtual_teacher.exceptions.DuplicateEntityException;
import com.example.virtual_teacher.exceptions.UnauthorizedOperationException;
import com.example.virtual_teacher.models.Homework;
import com.example.virtual_teacher.models.Note;
import com.example.virtual_teacher.models.User;
import com.example.virtual_teacher.repositories.contracts.HomeworkRepository;
import com.example.virtual_teacher.repositories.contracts.NoteRepository;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.ArrayList;

@ExtendWith(MockitoExtension.class)
public class HomeworkServiceImplTests {

    @Mock
    HomeworkRepository mockRepository;

    @InjectMocks
    HomeworkServiceImpl service;

    @Test
    void getHomeworkById_should_callRepository(){
        //Arrange
        Homework mockHomework = Helpers.createMockHomework();
        Mockito.when(mockRepository.getHomeworkById(mockHomework.getId())).thenReturn(mockHomework);

        // Act
        service.getHomeworkById(mockHomework.getId());

        // Assert
        Mockito.verify(mockRepository, Mockito.times(1))
                .getHomeworkById(mockHomework.getId());
    }

    @Test
    void getByUserId_should_callRepository(){
        //Arrange
        User mockUser = Helpers.createMockStudent();
        Mockito.when(mockRepository.getByUserId(mockUser.getId())).thenReturn(new ArrayList<>());

        // Act
        service.getByUserId(mockUser.getId());

        // Assert
        Mockito.verify(mockRepository, Mockito.times(1))
                .getByUserId(mockUser.getId());
    }
    
    @Test
    void getByTeacherId_should_callRepository_when_initiatorIsTeacher(){
        //Arrange
        User mockUser = Helpers.createMockTeacher();
        Mockito.when(mockRepository.getByTeacherId(mockUser.getId())).thenReturn(new ArrayList<>());

        // Act
        service.getByTeacherId(mockUser);

        // Assert
        Mockito.verify(mockRepository, Mockito.times(1))
                .getByTeacherId(mockUser.getId());
    } 
    
    @Test
    void getByTeacherId_should_callRepository_when_initiatorIsAdmin(){
        //Arrange
        User mockUser = Helpers.createMockAdmin();
        Mockito.when(mockRepository.getByTeacherId(mockUser.getId())).thenReturn(new ArrayList<>());

        // Act
        service.getByTeacherId(mockUser);

        // Assert
        Mockito.verify(mockRepository, Mockito.times(1))
                .getByTeacherId(mockUser.getId());
    }
    
    @Test
    void getByTeacherId_should_throwException_when_initiatorIsNotAdminOrTeacher(){
        //Arrange
        User mockUser = Helpers.createMockStudent();


        // Act, Assert
        Assertions.assertThrows(UnauthorizedOperationException.class,
                () -> service.getByTeacherId(mockUser));
    }

    @Test
    void create_should_callRepository(){
        //Arrange
        Homework mockHomework = Helpers.createMockHomework();

        // Act
        service.create(mockHomework);

        // Assert
        Mockito.verify(mockRepository, Mockito.times(1))
                .create(mockHomework);
    }

    @Test
    void update_should_callRepository(){
        //Arrange
        Homework mockHomework = Helpers.createMockHomework();

        // Act
        service.update(mockHomework);

        // Assert
        Mockito.verify(mockRepository, Mockito.times(1))
                .update(mockHomework);
    }

    @Test
    void delete_should_callRepository(){
        //Arrange
        Homework mockHomework = Helpers.createMockHomework();

        // Act
        service.softDelete(mockHomework.getId());

        // Assert
        Mockito.verify(mockRepository, Mockito.times(1))
                .softDelete(mockHomework.getId());
    }

}
