package com.example.virtual_teacher.models;

import com.fasterxml.jackson.annotation.JsonIgnore;

import javax.persistence.*;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Positive;
import java.time.LocalDate;

@Entity
@Table(name = "lectures_comments")
public class LectureComment {

    @JsonIgnore
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "lecture_comment_id")
    private int id;

    @NotNull
    @Positive
    @Column(name = "lecture_id")
    private int lectureId;

    @NotNull
    @JoinColumn(name = "user_id")
    @ManyToOne
    private User user;

    @Column(name = "content")
    @NotNull
    private String content;

    @Column(name = "creation_date")
    @NotNull
    private LocalDate creationDate;

    @JsonIgnore
    @Column(name = "is_deleted")
    private boolean isDeleted;

    public LectureComment() {
        creationDate = LocalDate.now();
    }

    public User getUser() {
        return user;
    }

    public void setUser(User user) {
        this.user = user;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public int getLectureId() {
        return lectureId;
    }

    public void setLectureId(int lectureId) {
        this.lectureId = lectureId;
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

    public boolean isDeleted() {
        return isDeleted;
    }

    public void setDeleted(boolean deleted) {
        isDeleted = deleted;
    }
}
