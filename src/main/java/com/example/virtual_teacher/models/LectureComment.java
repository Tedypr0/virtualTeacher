package com.example.virtual_teacher.models;

import com.fasterxml.jackson.annotation.JsonIgnore;

import jakarta.persistence.*;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Positive;
import java.time.LocalDate;

@Entity
@Table(name = "lectures_comments")
public class LectureComment implements PublicIdentifiable {

    @JsonIgnore
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "lecture_comment_id")
    private int id;

    @Column(name = "public_id", nullable = false, unique = true, updatable = false, length = 36)
    private String publicId;

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

    @PrePersist
    private void ensurePublicId() {
        PublicIdSupport.ensureAssigned(this);
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

    @Override
    public String getPublicId() {
        return publicId;
    }

    @Override
    public void setPublicId(String publicId) {
        this.publicId = publicId;
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
