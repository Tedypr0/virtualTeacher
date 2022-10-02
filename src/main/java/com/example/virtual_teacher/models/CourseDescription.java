package com.example.virtual_teacher.models;


import com.fasterxml.jackson.annotation.JsonIgnore;

import javax.persistence.*;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Size;

@Entity
@Table(name = "courses_descriptions")
public class CourseDescription {

    @JsonIgnore
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "course_description_id")
    private int id;

    @NotNull
    @Size(min = 3, max = 1000, message = "Course description must be between 3 and 1000 symbols")
    @Column(name = "description")
    private String description;

    @JsonIgnore
    @NotNull
    @Column(name = "course_id")
    private int courseId;

    @JsonIgnore
    @NotNull
    @Column(name = "is_deleted")
    private boolean isDeleted;

    public CourseDescription() {
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
    public String toString(){
        return getDescription();
    }
}
