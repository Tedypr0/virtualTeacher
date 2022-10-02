package com.example.virtual_teacher.services;

import com.example.virtual_teacher.Helpers;
import com.example.virtual_teacher.exceptions.DuplicateEntityException;
import com.example.virtual_teacher.exceptions.EntityNotFoundException;
import com.example.virtual_teacher.exceptions.UnauthorizedOperationException;
import com.example.virtual_teacher.models.Course;
import com.example.virtual_teacher.models.Lecture;
import com.example.virtual_teacher.models.LectureDescription;
import com.example.virtual_teacher.models.User;
import com.example.virtual_teacher.repositories.contracts.LectureDescriptionRepository;
import com.example.virtual_teacher.repositories.contracts.LectureRepository;
import com.example.virtual_teacher.services.contracts.LectureDescriptionService;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.ArrayList;

@ExtendWith(MockitoExtension.class)
public class LectureServiceImplTests {

    @Mock
    LectureRepository mockRepository;

    @Mock
    LectureDescriptionRepository mockLectureDescriptionRepo;
    @InjectMocks
    LectureServiceImpl service;

    @InjectMocks
    LectureDescriptionServiceImpl mockLectureDescriptionService;

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
    void getById_should_returnLecture_when_matchExists() {
        // Arrange
        Lecture mockLecture = Helpers.createMockLecture();
        Mockito.when(mockRepository.getById(mockLecture.getId()))
                .thenReturn(mockLecture);
        // Act
        Lecture result = service.getById(mockLecture.getId());

        // Assert
        Assertions.assertAll(
                () -> Assertions.assertEquals(mockLecture.getId(), result.getId()),
                () -> Assertions.assertEquals(mockLecture.getTitle(), result.getTitle()),
                () -> Assertions.assertEquals(mockLecture.getTeacher(), result.getTeacher())
        );
    }

    @Test
    void getByCourseId_should_callRepository_when_matchExists() {
        Course mockCourse = Helpers.createMockCourse();

        Mockito.when(mockRepository.lecturesByCourseId(mockCourse.getId()))
                .thenReturn(new ArrayList<>());

        service.getByCourseId(mockCourse.getId());

        Mockito.verify(mockRepository, Mockito.times(1)).lecturesByCourseId(mockCourse.getId());
    }

    @Test
    void getByCourseId_should_throwException_when_matchDoesNotExist() {
        Course mockCourse = Helpers.createMockCourse();

        Mockito.when(mockRepository.lecturesByCourseId(mockCourse.getId()))
                .thenThrow(EntityNotFoundException.class);

        Assertions.assertThrows(EntityNotFoundException.class,
                () -> service.getByCourseId(mockCourse.getId()));
    }

    @Test
    void lectureCount_should_callRepository() {
        service.lectureCount();

        Mockito.verify(mockRepository, Mockito.times(1)).lectureCount();
    }

    @Test
    void create_should_callRepository_when_lectureWithTheSameTitleDoesNotExist() {
        //Arrange
        Lecture mockLecture = Helpers.createMockLecture();
        User mockUser = mockLecture.getTeacher();

        Mockito.when(mockRepository.getByTitle(mockLecture.getTitle())).thenThrow(EntityNotFoundException.class);

        service.create(mockLecture, mockUser);
        // Act, Assert
        Mockito.verify(mockRepository, Mockito.times(1))
                .create(mockLecture);

    }

    @Test
    public void create_should_throwException_when_lectureWithSameTitleExists() {
        // Arrange
        Lecture mockLecture = Helpers.createMockLecture();

        Mockito.when(mockRepository.getByTitle(mockLecture.getTitle()))
                .thenReturn(mockLecture);

        // Act, Assert
        Assertions.assertThrows(DuplicateEntityException.class,
                () -> service.create(mockLecture, mockLecture.getTeacher()));
    }

    @Test
    public void create_should_throwException_when_userIsNotAuthorised() {
        // Arrange
        Lecture mockLecture = Helpers.createMockLecture();
        User mockStudent = Helpers.createMockStudent();
        mockLecture.setTeacher(mockStudent);

        // Act, Assert
        Assertions.assertThrows(UnauthorizedOperationException.class,
                () -> service.create(mockLecture, mockStudent));
    }

    @Test
    public void create_should_callRepository_when_descriptionIsNotEmpty() {
        // Arrange
        Lecture mockLecture = Helpers.createMockLecture();
        LectureDescription mockLectureDescription = Helpers.createMockLectureDescription();
        mockLecture.setDescription(mockLectureDescription);
        // Act
        Mockito.when(mockRepository.getByTitle(mockLecture.getTitle())).
                thenThrow(EntityNotFoundException.class);

        service.create(mockLecture, mockLecture.getTeacher());

        // Assert
        Mockito.verify(mockLectureDescriptionRepo, Mockito.times(1))
                .create(mockLectureDescription);
    }

    @Test
    public void update_should_throwException_when_lectureNotFound() {
        // Arrange
        Lecture mockLecture = Helpers.createMockLecture();
        User mockTeacher = Helpers.createMockTeacher();
        mockLecture.setDeleted(true);

        // Assert
        Assertions.assertThrows(EntityNotFoundException.class,
                () -> service.update(mockLecture, mockTeacher));
    }

    @Test
    public void update_should_throwException_when_userIsNotAdminOrOwner() {
        // Arrange
        User mockInitiator = Helpers.createMockTeacher();
        mockInitiator.setId(15);
        Lecture lecture = Helpers.createMockLecture();

        //Act & Assert
        Assertions.assertThrows(UnauthorizedOperationException.class, () ->
                service.update(lecture, mockInitiator));
    }

    @Test
    public void update_should_callRepository_when_userIsAdminAndNotOwner() {
        // Arrange
        User mockInitiator = Helpers.createMockAdmin();
        mockInitiator.setId(15);
        Lecture lecture = Helpers.createMockLecture();
        //Act
        service.update(lecture, mockInitiator);

        //Assert
        Mockito.verify(mockRepository, Mockito.times(1))
                .update(lecture);
    }

    @Test
    public void update_should_callRepository_when_userIsNotAdminAndIsOwner() {
        // Arrange
        User mockInitiator = Helpers.createMockTeacher();
        Lecture lecture = Helpers.createMockLecture();
        lecture.setDescription(Helpers.createMockLectureDescription());

        //Act
        service.update(lecture, mockInitiator);

        //Assert
        Mockito.verify(mockRepository, Mockito.times(1))
                .update(lecture);
    }

    @Test
    public void update_should_callRepository_when_descriptionID_isDifferentThan() {
        // Arrange
        Lecture mockLecture = Helpers.createMockLecture();
        LectureDescription mockDescription = Helpers.createMockLectureDescription();
        mockLecture.setDescription(mockDescription);

        // Act
        service.update(mockLecture, mockLecture.getTeacher());

        // Assert
        Mockito.verify(mockRepository, Mockito.times(1))
                .update(mockLecture);
    }

    @Test
    public void update_should_callRepository_when_descriptionIsNull() {
        // Arrange
        Lecture mockLecture = Helpers.createMockLecture();
        LectureDescription mockDescription = Helpers.createMockLectureDescription();
        mockLecture.setDescription(mockDescription);

        // Act
        service.update(mockLecture, mockLecture.getTeacher());

        // Assert
        Mockito.verify(mockRepository, Mockito.times(1))
                .update(mockLecture);
    }

}
