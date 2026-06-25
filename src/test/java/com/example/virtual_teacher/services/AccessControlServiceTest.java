package com.example.virtual_teacher.services;

import com.example.virtual_teacher.Helpers;
import com.example.virtual_teacher.exceptions.EntityNotFoundException;
import com.example.virtual_teacher.exceptions.UnauthorizedOperationException;
import com.example.virtual_teacher.models.Course;
import com.example.virtual_teacher.models.Lecture;
import com.example.virtual_teacher.models.User;
import com.example.virtual_teacher.services.contracts.CourseLectureService;
import com.example.virtual_teacher.services.contracts.UserService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import static org.junit.jupiter.api.Assertions.assertDoesNotThrow;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
class AccessControlServiceTest {

    @Mock
    private UserService userService;

    @Mock
    private CourseLectureService courseLectureService;

    private AccessControlService accessControlService;

    private User student;
    private User teacher;
    private User admin;
    private Course publishedCourse;
    private Course draftCourse;
    private Lecture lecture;

    @BeforeEach
    void setUp() {
        accessControlService = new AccessControlService(userService, courseLectureService);
        student = Helpers.createMockStudent();
        student.setId(10);
        teacher = Helpers.createMockTeacher();
        teacher.setId(20);
        admin = Helpers.createMockAdmin();
        admin.setId(30);
        publishedCourse = Helpers.createMockCourse();
        publishedCourse.setPublicId("course-published-uuid");
        publishedCourse.setTeacher(teacher);
        draftCourse = Helpers.createMockCourse();
        draftCourse.setDraft(true);
        draftCourse.setPublicId("course-draft-uuid");
        draftCourse.setTeacher(teacher);
        lecture = Helpers.createMockLecture();
        lecture.setPublicId("lecture-uuid");
        lecture.setCourse(publishedCourse);
        lecture.setAddedToCourse(true);
    }

    @Test
    void assertCanViewCourse_allowsAnyUserForPublishedCourse() {
        assertDoesNotThrow(() -> accessControlService.assertCanViewCourse(student, publishedCourse));
    }

    @Test
    void assertCanViewCourse_deniesStudentForDraftCourse() {
        assertThrows(EntityNotFoundException.class,
                () -> accessControlService.assertCanViewCourse(student, draftCourse));
    }

    @Test
    void assertCanViewCourse_allowsTeacherForOwnDraftCourse() {
        assertDoesNotThrow(() -> accessControlService.assertCanViewCourse(teacher, draftCourse));
    }

    @Test
    void assertCanViewLecture_deniesUnenrolledStudent() {
        when(userService.isEnrolled(publishedCourse.getId(), student.getId())).thenReturn(false);

        assertThrows(UnauthorizedOperationException.class,
                () -> accessControlService.assertCanViewLecture(student, publishedCourse, lecture));
    }

    @Test
    void assertCanViewLecture_allowsEnrolledStudent() {
        when(userService.isEnrolled(publishedCourse.getId(), student.getId())).thenReturn(true);

        assertDoesNotThrow(() -> accessControlService.assertCanViewLecture(student, publishedCourse, lecture));
    }

    @Test
    void assertCanViewLecture_allowsCourseTeacherWithoutEnrollment() {
        assertDoesNotThrow(() -> accessControlService.assertCanViewLecture(teacher, publishedCourse, lecture));
    }

    @Test
    void assertCanModifyUserProfile_deniesOtherUsers() {
        User otherStudent = Helpers.createMockStudent();
        otherStudent.setId(99);

        assertThrows(UnauthorizedOperationException.class,
                () -> accessControlService.assertCanModifyUserProfile(student, otherStudent));
    }

    @Test
    void assertCanModifyUserProfile_allowsSelf() {
        assertDoesNotThrow(() -> accessControlService.assertCanModifyUserProfile(student, student));
    }

    @Test
    void assertCanModifyUserProfile_allowsAdmin() {
        assertDoesNotThrow(() -> accessControlService.assertCanModifyUserProfile(admin, student));
    }

    @Test
    void assertCanGradeHomework_deniesStudent() {
        assertThrows(UnauthorizedOperationException.class,
                () -> accessControlService.assertCanGradeHomework(student, publishedCourse));
    }

    @Test
    void assertCanGradeHomework_allowsCourseTeacher() {
        assertDoesNotThrow(() -> accessControlService.assertCanGradeHomework(teacher, publishedCourse));
    }
}
