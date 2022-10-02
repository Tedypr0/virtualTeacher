package com.example.virtual_teacher.services;

import com.example.virtual_teacher.Helpers;
import com.example.virtual_teacher.models.Note;
import com.example.virtual_teacher.models.User;
import com.example.virtual_teacher.models.UserRole;
import com.example.virtual_teacher.repositories.contracts.NoteRepository;
import com.example.virtual_teacher.repositories.contracts.RoleRepository;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.ArrayList;

@ExtendWith(MockitoExtension.class)
public class NoteServiceImplTests {

    @Mock
    NoteRepository mockRepository;

    @InjectMocks
    NoteServiceImpl service;

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
        Note mockNote = Helpers.createMockNote();
        Mockito.when(mockRepository.getById(mockNote.getId())).thenReturn(mockNote);

        // Act
        service.getById(mockNote.getId());

        // Assert
        Mockito.verify(mockRepository, Mockito.times(1))
                .getById(mockNote.getId());
    }


    @Test
    void create_should_callRepository(){
        //Arrange
        Note mockNote = Helpers.createMockNote();
        Mockito.when(mockRepository.create(mockNote)).thenReturn(mockNote);

        // Act
        service.create(mockNote);

        // Assert
        Mockito.verify(mockRepository, Mockito.times(1))
                .create(mockNote);
    }

    @Test
    void update_should_callRepository(){
        //Arrange
        Note mockNote = Helpers.createMockNote();
        User initiator = Helpers.createMockStudent();
        Mockito.when(mockRepository.update(mockNote)).thenReturn(mockNote);

        // Act
        service.update(mockNote, initiator);

        // Assert
        Mockito.verify(mockRepository, Mockito.times(1))
                .update(mockNote);
    }

    @Test
    void delete_should_callRepository(){
        //Arrange
        Note mockNote = Helpers.createMockNote();
        User initiator = Helpers.createMockStudent();
        Mockito.when(mockRepository.delete(mockNote.getId())).thenReturn(mockNote);

        // Act
        service.delete(initiator, mockNote.getId());

        // Assert
        Mockito.verify(mockRepository, Mockito.times(1))
                .delete(mockNote.getId());
    }

    @Test
    void getByUserId_should_callRepository(){
        //Arrange
        Note mockNote = Helpers.createMockNote();
        User initiator = Helpers.createMockStudent();
        Mockito.when(mockRepository.getByUserId(initiator.getId())).thenReturn(new ArrayList<>());

        // Act
        service.getByUserId(initiator.getId());

        // Assert
        Mockito.verify(mockRepository, Mockito.times(1))
                .getByUserId(mockNote.getId());
    }
}
