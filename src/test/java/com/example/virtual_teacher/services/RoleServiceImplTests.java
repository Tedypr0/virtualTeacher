package com.example.virtual_teacher.services;

import com.example.virtual_teacher.Helpers;
import com.example.virtual_teacher.models.Topic;
import com.example.virtual_teacher.models.UserRole;
import com.example.virtual_teacher.repositories.contracts.RoleRepository;
import com.example.virtual_teacher.repositories.contracts.TopicRepository;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.ArrayList;

@ExtendWith(MockitoExtension.class)
public class RoleServiceImplTests {
    @Mock
    RoleRepository mockRepository;

    @InjectMocks
    RoleServiceImpl service;

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
        UserRole mockRole = Helpers.createMockRoleAdmin();
        Mockito.when(mockRepository.getById(mockRole.getId())).thenReturn(mockRole);

        // Act
        service.getById(mockRole.getId());

        // Assert
        Mockito.verify(mockRepository, Mockito.times(1))
                .getById(mockRole.getId());
    }

    @Test
    void getRoleName_should_callRepository(){
        //Arrange
        UserRole mockRole = Helpers.createMockRoleAdmin();
        Mockito.when(mockRepository.getByRoleName(mockRole.getRole())).thenReturn(mockRole);

        // Act
        service.getByRoleName(mockRole.getRole());

        // Assert
        Mockito.verify(mockRepository, Mockito.times(1))
                .getByRoleName(mockRole.getRole());
    }

    @Test
    void create_should_callRepository(){
        //Arrange
        UserRole mockRole = Helpers.createMockRoleAdmin();
        Mockito.when(mockRepository.create(mockRole)).thenReturn(mockRole);

        // Act
        service.create(mockRole);

        // Assert
        Mockito.verify(mockRepository, Mockito.times(1))
                .create(mockRole);
    }

    @Test
    void update_should_callRepository(){
        //Arrange
        UserRole mockRole = Helpers.createMockRoleAdmin();
        Mockito.when(mockRepository.update(mockRole)).thenReturn(mockRole);

        // Act
        service.update(mockRole);

        // Assert
        Mockito.verify(mockRepository, Mockito.times(1))
                .update(mockRole);
    }

    @Test
    void delete_should_callRepository(){
        //Arrange
        UserRole mockRole = Helpers.createMockRoleAdmin();
        Mockito.when(mockRepository.delete(mockRole.getId())).thenReturn(mockRole);

        // Act
        service.delete(mockRole.getId());

        // Assert
        Mockito.verify(mockRepository, Mockito.times(1))
                .delete(mockRole.getId());
    }


}
