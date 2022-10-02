package com.example.virtual_teacher.models;

import org.checkerframework.checker.signature.qual.Identifier;

import javax.persistence.*;
import java.util.Objects;

@Entity
@Table(name = "users_courses")
public class UsersCourses {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "user_course_id", nullable = false)
    private int id;

    @ManyToOne
    @JoinColumn(name = "course_id", nullable = false)
    private Course course;

    @Column(name = "user_id", nullable = false)
    private int userId;

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public Course getCourse() {
        return course;
    }

    public void setCourse(Course course) {
        this.course = course;
    }

    public int getUserId() {
        return userId;
    }

    public void setUserId(int userId) {
        this.userId = userId;
    }
}
