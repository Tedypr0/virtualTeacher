package com.example.virtual_teacher.services;

import com.example.virtual_teacher.Helpers;
import com.example.virtual_teacher.exceptions.UnauthorizedOperationException;
import com.example.virtual_teacher.models.Lecture;
import com.example.virtual_teacher.models.LectureComment;
import com.example.virtual_teacher.models.Note;
import com.example.virtual_teacher.models.User;
import com.example.virtual_teacher.repositories.contracts.LectureCommentRepository;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.ArrayList;

@ExtendWith(MockitoExtension.class)
public class LectureCommentServiceImplTests {

    @Mock
    LectureCommentRepository mockRepository;

    @InjectMocks
    LectureCommentServiceImpl service;


    @Test
    void getAll_should_callRepository(){
        Lecture mockLecture = Helpers.createMockLecture();
        //Arrange
        Mockito.when(mockRepository.getAll(mockLecture.getId())).thenReturn(new ArrayList<>());

        // Act
        service.getAll(mockLecture.getId());

        // Assert
        Mockito.verify(mockRepository, Mockito.times(1))
                .getAll(mockLecture.getId());
    }

    @Test
    void getById_should_callRepository(){
        //Arrange
        LectureComment mockLectureComment = Helpers.createMockLectureComment();
        Mockito.when(mockRepository.getById(mockLectureComment.getId())).thenReturn(mockLectureComment);

        // Act
        service.getById(mockLectureComment.getId());

        // Assert
        Mockito.verify(mockRepository, Mockito.times(1))
                .getById(mockLectureComment.getId());
    }

    @Test
    void create_should_callRepository(){
        //Arrange
        LectureComment mockLectureComment = Helpers.createMockLectureComment();
        Mockito.when(mockRepository.create(mockLectureComment)).thenReturn(mockLectureComment);

        // Act
        service.create(mockLectureComment);

        // Assert
        Mockito.verify(mockRepository, Mockito.times(1))
                .create(mockLectureComment);
    }

    @Test
    void update_should_throwException_when_initiatorIsNotAdminAndOwner(){
        //Arrange
        LectureComment mockLectureComment = Helpers.createMockLectureComment();
        User initiator = Helpers.createMockStudent();

        initiator.setId(123);

        Assertions.assertThrows(UnauthorizedOperationException.class,
                () -> service.update(initiator, mockLectureComment));
    }

    @Test
    void update_should_callRepository_when_initiatorIsNotAdminAndIsOwner(){
        //Arrange
        LectureComment mockLectureComment = Helpers.createMockLectureComment();
        User initiator = Helpers.createMockStudent();

        service.update(initiator, mockLectureComment);

        Mockito.verify(mockRepository, Mockito.times(1))
                .update(mockLectureComment);
    }

    @Test
    void update_should_callRepository_when_initiatorIsAdminAndIsNotOwner(){
        //Arrange
        LectureComment mockLectureComment = Helpers.createMockLectureComment();
        User initiator = Helpers.createMockAdmin();
        initiator.setId(123);

        service.update(initiator, mockLectureComment);

        Mockito.verify(mockRepository, Mockito.times(1))
                .update(mockLectureComment);
    }

    @Test
    void delete_should_throwException_when_initiatorIsNotAdminAndOwner(){
        //Arrange
        LectureComment mockLectureComment = Helpers.createMockLectureComment();
        User initiator = Helpers.createMockStudent();

        Mockito.when(mockRepository.getById(mockLectureComment.getId())).thenReturn(mockLectureComment);

        initiator.setId(123);

        Assertions.assertThrows(UnauthorizedOperationException.class,
                () -> service.delete(initiator, mockLectureComment.getId()));
    }

    @Test
    void delete_should_callRepository_when_initiatorIsNotAdminAndIsOwner(){
        //Arrange
        LectureComment mockLectureComment = Helpers.createMockLectureComment();
        User initiator = Helpers.createMockStudent();

        Mockito.when(mockRepository.getById(mockLectureComment.getId())).thenReturn(mockLectureComment);

        service.delete(initiator, mockLectureComment.getId());

        Mockito.verify(mockRepository, Mockito.times(1))
                .delete(mockLectureComment.getId());
    }

    @Test
    void delete_should_callRepository_when_initiatorIsAdminAndIsNotOwner(){
        //Arrange
        LectureComment mockLectureComment = Helpers.createMockLectureComment();
        User initiator = Helpers.createMockAdmin();
        initiator.setId(123);

        Mockito.when(mockRepository.getById(mockLectureComment.getId())).thenReturn(mockLectureComment);

        service.delete(initiator, mockLectureComment.getId());

        Mockito.verify(mockRepository, Mockito.times(1))
                .delete(mockLectureComment.getId());
    }
}
