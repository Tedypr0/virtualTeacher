package com.example.virtual_teacher;

import com.example.virtual_teacher.models.*;

import java.sql.Date;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;

public class Helpers {

    public static Lecture createMockLecture(){
        Lecture mockLecture = new Lecture();
        mockLecture.setId(1);
        mockLecture.setTeacher(createMockTeacher());
        mockLecture.setTitle("mockTitle");
        mockLecture.setVideo(createMockVideo());
        return mockLecture;
    }

    public static User createMockTeacher(){
        User user = new User();
        user.setId(1);
        user.setActive(true);
        user.setDate(LocalDateTime.now());
        user.setEmail("user@user.com");
        user.setFirstName("Neda");
        user.setLastName("Marinova");
        user.setPassword("123456789T");
        user.setRole(createMockRoleTeacher());
        return user;
    }

    public static User createMockStudent(){
        User user = new User();
        user.setId(1);
        user.setActive(true);
        user.setDate(LocalDateTime.now());
        user.setEmail("user@user.com");
        user.setFirstName("Neda");
        user.setLastName("Marinova");
        user.setPassword("123456789T");
        user.setRole(createMockRoleStudent());
        return user;
    }

    public static UserRole createMockRoleTeacher(){
        UserRole userRole = new UserRole();
        userRole.setId(1);
        userRole.setRole("Teacher");
        return userRole;
    }

    public static UserRole createMockRoleStudent(){
        UserRole userRole = new UserRole();
        userRole.setId(2);
        userRole.setRole("Student");
        return userRole;
    }

    public static User createMockAdmin(){
        User mockUser = new User();
        mockUser.setId(1);
        mockUser.setFirstName("mockFirstName");
        mockUser.setLastName("mockLastName");
        mockUser.setPassword("mockPassword");
        mockUser.setEmail("mockEmail@telerikacademy.com");
        mockUser.setRole(createMockRoleAdmin());
        mockUser.setDate(LocalDateTime.now());
        return mockUser;
    }

    public static UserRole createMockRoleAdmin(){
        UserRole userRole = new UserRole();
        userRole.setId(3);
        userRole.setRole("Admin");
        return userRole;
    }

    public static Video createMockVideo(){
        Video video = new Video();
        video.setVideoId(1);
        video.setDeleted(false);
        video.setVideoUrl("9iMGFqMmUFs");
        return video;
    }

    public static LectureDescription createMockLectureDescription(){
        LectureDescription lectureDescription = new LectureDescription();
        lectureDescription.setId(1);
        lectureDescription.setLectureId(1);
        lectureDescription.setDescription("This is the mock description");
        lectureDescription.setDeleted(false);
        return lectureDescription;
    }

    public static MotivationalLetter createMockMotivationalLetter(){
        MotivationalLetter motivationalLetter = new MotivationalLetter();
        motivationalLetter.setUserId(1);
        motivationalLetter.setMotivationalLetter("mockLetter");
        motivationalLetter.setId(1);
        return motivationalLetter;
    }

    public static UsersCourses createMockUsersCourses(){
        UsersCourses mockUsersCourses = new UsersCourses();
        mockUsersCourses.setCourse(createMockCourse());
        mockUsersCourses.setId(1);
        mockUsersCourses.setUserId(1);
        return mockUsersCourses;
    }

    public static Course createMockCourse(){
        Course course = new Course();
        course.setId(1);
        course.setTitle("mockTitle");
        course.setTopic(createMockTopic());
        course.setAvgRating(4.0);
        course.setDescription(createMockCourseDescription());
        course.setTeacher(createMockTeacher());
        course.setDeleted(false);
        course.setDraft(false);
        course.setStartingDate(new Date(System.currentTimeMillis()));
        return course;
    }

    public static Topic createMockTopic(){
        Topic mockTopic = new Topic();
        mockTopic.setId(1);
        mockTopic.setTopicName("mockTopicName");
        mockTopic.setImgUrl("mockImgUrl");
        return mockTopic;
    }

    public static CourseDescription createMockCourseDescription(){
        CourseDescription mockCourseDescription = new CourseDescription();
        mockCourseDescription.setCourseId(1);
        mockCourseDescription.setDescription("mockDescription");
        mockCourseDescription.setDeleted(false);
        mockCourseDescription.setId(1);
        return mockCourseDescription;
    }

    public static Rating createMockRating(){
        Rating mockRating = new Rating();
        mockRating.setId(1);
        mockRating.setRatingScore(5);
        mockRating.setCourseId(1);
        mockRating.setCreationDate(new Date(System.currentTimeMillis()));
        mockRating.setReview("mockReview");
        mockRating.setUser(createMockStudent());
        return mockRating;
    }

    public static Note createMockNote(){
        Note mockNote = new Note();
        mockNote.setId(1);
        mockNote.setNote("mockNote");
        mockNote.setDeleted(false);
        mockNote.setUserId(1);
        mockNote.setLecture(createMockLecture());
        return mockNote;
    }

    public static Homework createMockHomework(){
        Homework mockHomework = new Homework();
        mockHomework.setId(1);
        mockHomework.setUser(createMockStudent());
        mockHomework.setLecture(createMockLecture());
        mockHomework.setGrade(4);
        mockHomework.setHomeworkName("mockHomeworkName");
        mockHomework.setDeleted(false);
        return mockHomework;
    }

    public static CourseComment createMockCourseComment(){
        CourseComment mockCourseComment = new CourseComment();
        mockCourseComment.setId(1);
        mockCourseComment.setCourseId(1);
        mockCourseComment.setUser(createMockStudent());
        mockCourseComment.setCreationDate(LocalDate.now());
        mockCourseComment.setDeleted(false);
        mockCourseComment.setContent("mockContent");
        return mockCourseComment;
    }

    public static CourseLecture createMockCourseLecture(){
        CourseLecture mockCourseLecture = new CourseLecture();

        mockCourseLecture.setId(1);
        mockCourseLecture.setCourseId(1);
        mockCourseLecture.setLectureId(1);
        return mockCourseLecture;
    }

    public static LectureComment createMockLectureComment(){
        LectureComment mockLectureComment = new LectureComment();
        mockLectureComment.setId(1);
        mockLectureComment.setContent("mockContent");
        mockLectureComment.setCreationDate(LocalDate.now());
        mockLectureComment.setLectureId(1);
        mockLectureComment.setDeleted(false);
        mockLectureComment.setUser(createMockStudent());
        return mockLectureComment;
    }
}
