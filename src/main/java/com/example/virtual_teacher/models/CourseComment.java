package com.example.virtual_teacher.models;

import com.fasterxml.jackson.annotation.JsonIgnore;

import javax.persistence.*;
import javax.validation.constraints.NotNull;
import java.time.LocalDate;
import java.util.Objects;

@Entity
@Table(name = "courses_comments")
public class CourseComment {

    @JsonIgnore
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "course_comment_id")
    private int id;

    @NotNull
    @Column(name = "content")
    private String content;

    @NotNull
    @Column(name = "creation_date")
    private LocalDate creationDate;

    @NotNull
    @JoinColumn(name = "user_id")
    @ManyToOne
    private User user;

    @NotNull
    @Column(name = "course_id")
    private int courseId;

    @JsonIgnore
    @NotNull
    @Column(name = "is_deleted")
    private boolean isDeleted;

    public CourseComment() {
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getContent() {
        return content;
    }

    public void setContent(String content) {
        this.content = content;
    }

    public LocalDate getCreationDate() {
        return creationDate;
    }

    public void setCreationDate(LocalDate creationDate) {
        this.creationDate = creationDate;
    }

    public User getUser() {
        return user;
    }

    public void setUser(User user) {
        this.user = user;
    }

    public int getCourseId() {
        return courseId;
    }

    public void setCourseId(int courseId) {
        this.courseId = courseId;
    }

    public boolean isDeleted() {
        return isDeleted;
    }

    public void setDeleted(boolean deleted) {
        isDeleted = deleted;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        CourseComment comments = (CourseComment) o;
        return id == comments.id && courseId == comments.courseId && user == comments.user && Objects.equals(content, comments.content);
    }

    @Override
    public int hashCode() {
        return Objects.hash(id, content, courseId, user);
    }
}
