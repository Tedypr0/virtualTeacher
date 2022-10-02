package com.example.virtual_teacher.models;

import com.fasterxml.jackson.annotation.JsonIgnore;

import javax.persistence.*;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Size;

@Entity
@Table(name = "lectures_descriptions")
public class LectureDescription {

    @JsonIgnore
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "lecture_description_id")
    private int id;

    @NotNull
    @Column(name = "description")
    @Size(max = 1000, message = "Lecture description must be maximum 1000 symbols")
    private String description;

    @JsonIgnore
    @NotNull
    @Id
    @Column(name = "lecture_id")
    private int lectureId;

    @JsonIgnore
    @Column(name = "is_deleted")
    private boolean isDeleted;

    public LectureDescription() {
    }

    public boolean isDeleted() {
        return isDeleted;
    }

    public void setDeleted(boolean deleted) {
        isDeleted = deleted;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public int getLectureId() {
        return lectureId;
    }

    public void setLectureId(int lectureId) {
        this.lectureId = lectureId;
    }
}
