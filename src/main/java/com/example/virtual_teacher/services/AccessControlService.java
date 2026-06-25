package com.example.virtual_teacher.services;

import com.example.virtual_teacher.exceptions.EntityNotFoundException;
import com.example.virtual_teacher.exceptions.UnauthorizedOperationException;
import com.example.virtual_teacher.models.*;
import com.example.virtual_teacher.services.contracts.CourseLectureService;
import com.example.virtual_teacher.services.contracts.UserService;
import org.springframework.stereotype.Service;

@Service
public class AccessControlService {

    private static final String ACCESS_DENIED = "You are not allowed to access this resource.";
    private static final String ADMIN_ONLY = "Only administrators can perform this action.";

    private final UserService userService;
    private final CourseLectureService courseLectureService;

    public AccessControlService(UserService userService, CourseLectureService courseLectureService) {
        this.userService = userService;
        this.courseLectureService = courseLectureService;
    }

    public void assertAdmin(User authUser) {
        if (!authUser.isAdmin()) {
            throw new UnauthorizedOperationException(ADMIN_ONLY);
        }
    }

    public void assertCanViewCourse(User authUser, Course course) {
        assertCourseExists(course);
        if (course.isDraft() && !canManageCourse(authUser, course)) {
            throw new EntityNotFoundException("Course", "publicId", course.getPublicId());
        }
    }

    public void assertCanModifyCourse(User authUser, Course course) {
        assertCourseExists(course);
        if (!canManageCourse(authUser, course)) {
            throw new UnauthorizedOperationException(ACCESS_DENIED);
        }
    }

    public void assertCanViewLecture(User authUser, Course course, Lecture lecture) {
        assertCanViewCourse(authUser, course);
        assertLectureBelongsToCourse(lecture, course);
        if (canManageCourse(authUser, course)) {
            return;
        }
        if (!lecture.isAddedToCourse()) {
            throw new EntityNotFoundException("Lecture", lecture.getId());
        }
        if (userService.isEnrolled(course.getId(), authUser.getId())) {
            return;
        }
        throw new UnauthorizedOperationException(ACCESS_DENIED);
    }

    public void assertCanModifyLecture(User authUser, Course course, Lecture lecture) {
        assertCanModifyCourse(authUser, course);
        if (lecture == null || lecture.isDeleted()) {
            throw new EntityNotFoundException("Lecture", "unknown");
        }
        if (lecture.getTeacher() != null && authUser.getId() == lecture.getTeacher().getId()) {
            return;
        }
        assertLectureBelongsToCourse(lecture, course);
    }

    public void assertCanViewUserProfile(User authUser, User targetUser) {
        if (authUser.isAdmin() || authUser.getId() == targetUser.getId()) {
            return;
        }
        throw new UnauthorizedOperationException(ACCESS_DENIED);
    }

    public void assertCanModifyUserProfile(User authUser, User targetUser) {
        assertCanViewUserProfile(authUser, targetUser);
    }

    public void assertCanViewProfileImage(User authUser, User targetUser) {
        if (authUser.isAdmin() || authUser.getId() == targetUser.getId()) {
            return;
        }
        if (authUser.isTeacher()) {
            return;
        }
        if (targetUser.isTeacher()) {
            return;
        }
        throw new UnauthorizedOperationException(ACCESS_DENIED);
    }

    public void assertCanViewNote(User authUser, Note note) {
        if (note.isDeleted()) {
            throw new EntityNotFoundException("Note", note.getId());
        }
        if (authUser.isAdmin() || authUser.getId() == note.getUserId()) {
            return;
        }
        throw new UnauthorizedOperationException(ACCESS_DENIED);
    }

    public void assertCanModifyNote(User authUser, Note note) {
        assertCanViewNote(authUser, note);
    }

    public void assertCanViewCourseComment(User authUser, Course course, CourseComment comment) {
        assertCanViewCourse(authUser, course);
        if (comment.isDeleted()) {
            throw new EntityNotFoundException("CourseComment", comment.getId());
        }
        if (comment.getCourseId() != course.getId()) {
            throw new EntityNotFoundException("CourseComment", comment.getId());
        }
    }

    public void assertCanModifyCourseComment(User authUser, CourseComment comment) {
        if (authUser.isAdmin() || authUser.getId() == comment.getUser().getId()) {
            return;
        }
        throw new UnauthorizedOperationException(ACCESS_DENIED);
    }

    public void assertCanViewLectureComment(User authUser, Course course, Lecture lecture, LectureComment comment) {
        assertCanViewLecture(authUser, course, lecture);
        if (comment.isDeleted()) {
            throw new EntityNotFoundException("LectureComment", comment.getId());
        }
        if (comment.getLectureId() != lecture.getId()) {
            throw new EntityNotFoundException("LectureComment", comment.getId());
        }
    }

    public void assertCanModifyLectureComment(User authUser, LectureComment comment) {
        if (authUser.isAdmin() || authUser.getId() == comment.getUser().getId()) {
            return;
        }
        throw new UnauthorizedOperationException(ACCESS_DENIED);
    }

    public void assertCanGradeHomework(User authUser, Course course) {
        assertCanModifyCourse(authUser, course);
    }

    public void assertCanEnroll(User authUser, Course course) {
        assertCourseExists(course);
        if (course.isDraft()) {
            throw new UnauthorizedOperationException("This course is not available for enrollment.");
        }
        if (authUser.isTeacher() && !authUser.isAdmin()) {
            throw new UnauthorizedOperationException("Teachers cannot enroll in courses.");
        }
    }

    private boolean canManageCourse(User authUser, Course course) {
        return authUser.isAdmin()
                || (course.getTeacher() != null && authUser.getId() == course.getTeacher().getId());
    }

    private void assertCourseExists(Course course) {
        if (course == null || course.isDeleted()) {
            throw new EntityNotFoundException("Course", "unknown");
        }
    }

    private void assertLectureBelongsToCourse(Lecture lecture, Course course) {
        if (lecture == null || lecture.isDeleted()) {
            throw new EntityNotFoundException("Lecture", "unknown");
        }
        if (lecture.getCourse() != null && lecture.getCourse().getId() == course.getId()) {
            return;
        }
        if (courseLectureService.isAddedToCourse(course.getId(), lecture.getId())) {
            return;
        }
        throw new EntityNotFoundException("Lecture", lecture.getId());
    }
}
