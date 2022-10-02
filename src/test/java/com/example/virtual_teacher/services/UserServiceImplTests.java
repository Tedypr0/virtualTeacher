package com.example.virtual_teacher.services;

import com.example.virtual_teacher.exceptions.DuplicateEntityException;
import com.example.virtual_teacher.exceptions.EntityNotFoundException;
import com.example.virtual_teacher.models.MotivationalLetter;
import com.example.virtual_teacher.models.User;
import com.example.virtual_teacher.models.UsersCourses;
import com.example.virtual_teacher.repositories.contracts.LectureRepository;
import com.example.virtual_teacher.repositories.contracts.UserRepository;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.junit.jupiter.MockitoExtension;
import com.example.virtual_teacher.Helpers;

import java.util.ArrayList;

@ExtendWith(MockitoExtension.class)
public class UserServiceImplTests {

    @Mock
    UserRepository mockRepository;

    @InjectMocks
    UserServiceImpl service;

    @Test
    void getAll_should_callRepository() {
        // Arrange
        Mockito.when(mockRepository.getAll())
                .thenReturn(new ArrayList<>());

        // Act
        service.getAll();

        // Assert
        Mockito.verify(mockRepository, Mockito.times(1))
                .getAll();
    }

    @Test
    public void getById_should_returnUser_when_matchExist() {
        // Arrange
        User mockUser = Helpers.createMockStudent();
        Mockito.when(mockRepository.getById(mockUser.getId()))
                .thenReturn(mockUser);
        // Act
        User result = service.getById(mockUser.getId());

        // Assert
        Assertions.assertAll(
                () -> Assertions.assertEquals(mockUser.getId(), result.getId()),
                () -> Assertions.assertEquals(mockUser.getFirstName(), result.getFirstName()),
                () -> Assertions.assertEquals(mockUser.getLastName(), result.getLastName()),
                () -> Assertions.assertEquals(mockUser.getEmail(), result.getEmail()),
                () -> Assertions.assertEquals(mockUser.getPassword(), result.getPassword()),
                () -> Assertions.assertEquals(mockUser.getRole(), result.getRole())
        );
    }

    @Test
    public void getByEmail_should_returnUser_when_matchExist() {
        // Arrange
        User mockUser = Helpers.createMockStudent();
        Mockito.when(mockRepository.getByEmail(mockUser.getEmail()))
                .thenReturn(mockUser);
        // Act
        User result = service.getByEmail(mockUser.getEmail());

        // Assert
        Assertions.assertAll(
                () -> Assertions.assertEquals(mockUser.getId(), result.getId()),
                () -> Assertions.assertEquals(mockUser.getFirstName(), result.getFirstName()),
                () -> Assertions.assertEquals(mockUser.getLastName(), result.getLastName()),
                () -> Assertions.assertEquals(mockUser.getEmail(), result.getEmail()),
                () -> Assertions.assertEquals(mockUser.getPassword(), result.getPassword()),
                () -> Assertions.assertEquals(mockUser.getRole(), result.getRole())
        );
    }

    @Test
    public void create_should_throwException_when_userWithSameEmailExists() {
        // Arrange
        User mockUser = Helpers.createMockStudent();

        Mockito.when(mockRepository.getByEmail(mockUser.getEmail()))
                .thenReturn(mockUser);

        // Act, Assert
        Assertions.assertThrows(DuplicateEntityException.class, () -> service.create(mockUser));
    }

    @Test
    public void create_should_callRepository_when_userWithSameEmailDoesNotExist() {
        // Arrange
        User mockUser = Helpers.createMockStudent();

        Mockito.when(mockRepository.getByEmail(mockUser.getEmail()))
                .thenThrow(EntityNotFoundException.class);

        service.create(mockUser);
        // Act, Assert
        Mockito.verify(mockRepository, Mockito.times(1))
                .create(mockUser);
    }

    @Test
    void update_should_callRepository() {
        // Arrange
        User mockUser = Helpers.createMockStudent();
        User initiator = Helpers.createMockAdmin();

        // Act
        service.update(initiator, mockUser);

        // Assert
        Mockito.verify(mockRepository, Mockito.times(1))
                .update(mockUser);
    }

    @Test
    void delete_should_callRepository_always(){
        User mockUser = Helpers.createMockStudent();

        // Act
        service.delete(mockUser.getId());

        // Assert
        Mockito.verify(mockRepository, Mockito.times(1))
                .delete(mockUser.getId());
    }

    @Test
    void createTeacherApplication_should_callRepository_when_applicationDoesNotExist(){
        User initiator = Helpers.createMockStudent();
        MotivationalLetter mockLetter = Helpers.createMockMotivationalLetter();
        // Act

        Mockito.when(mockRepository.applicationExists(initiator.getId())).thenReturn(false);

        service.createTeacherApplication(initiator.getId(), mockLetter);

        Mockito.verify(mockRepository, Mockito.times(1))
                .createTeacherApplication(initiator.getId(), mockLetter);
    }


    @Test
    void deleteTeacherApplication_should_callRepository(){
        User mockUser = Helpers.createMockStudent();

        // Act
        service.deleteTeacherApplication(mockUser.getId());

        // Assert
        Mockito.verify(mockRepository, Mockito.times(1))
                .deleteTeacherApplication(mockUser.getId());
    }

    @Test
    void getAllTeacherApplications_should_callRepository(){

        // Act
        service.getAllTeacherApplications();

        // Assert
        Mockito.verify(mockRepository, Mockito.times(1))
                .getAllTeacherApplications();
    }

    @Test
    void isEnrolled_should_callRepository() {
        User mockUser = Helpers.createMockStudent();

        // Act
        service.isEnrolled(1, mockUser.getId());

        // Assert
        Mockito.verify(mockRepository, Mockito.times(1))
                .isEnrolled(1, mockUser.getId());
    }


    @Test
    void enrollToCourse_should_callRepository(){
        UsersCourses mockUsersCourses = Helpers.createMockUsersCourses();

        // Act
        service.enrollToCourse(mockUsersCourses);

        // Assert
        Mockito.verify(mockRepository, Mockito.times(1))
                .enrollToCourse(mockUsersCourses);
    }

    @Test
    void applicationExists_should_callRepository(){
        service.applicationExists(1);

        Mockito.verify(mockRepository, Mockito.times(1)).applicationExists(1);
    }
}
