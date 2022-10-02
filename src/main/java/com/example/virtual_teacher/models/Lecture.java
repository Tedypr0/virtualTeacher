package com.example.virtual_teacher.models;

import com.fasterxml.jackson.annotation.JsonIgnore;
import org.checkerframework.checker.optional.qual.OptionalBottom;
import org.springframework.data.jpa.repository.Query;

import javax.persistence.*;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Size;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.Set;

@Entity
@Table(name = "lectures")
public class Lecture {

    @JsonIgnore
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "lecture_id")
    private int id;

    @Column(name = "title")
    @NotNull
    @Size(min = 5, max = 50, message = "Lecture title must be between 5 and 50 symbols")
    private String title;

    @OneToOne
    @JoinColumn(name = "lecture_id")
    private LectureDescription description;

    @OneToOne
    @JoinColumn(name = "video_id")
    private Video video;

    @JsonIgnore
    @Column(name = "is_deleted")
    private boolean isDeleted;

    @JsonIgnore
    @OneToMany(fetch = FetchType.EAGER)
    @JoinColumn(name = "lecture_id")
    @OrderBy("creationDate")
    private Set<LectureComment> comments;

    @OneToOne
    @JoinColumn(name = "user_id")
    private User teacher;

    @ManyToOne
    @JoinTable(name = "courses_lectures",joinColumns = @JoinColumn(name = "lecture_id"), inverseJoinColumns = @JoinColumn(name = "course_id"))
    private Course course;

    @Column(name = "is_added_to_course")
    private boolean isAddedToCourse;

    public Lecture() {}

    public boolean isAddedToCourse() {
        return isAddedToCourse;
    }

    public void setAddedToCourse(boolean addedToCourse) {
        isAddedToCourse = addedToCourse;
    }

    public User getTeacher() {
        return teacher;
    }

    public void setTeacher(User teacher) {
        this.teacher = teacher;
    }

    public LectureDescription getDescription() {
        return description;
    }

    public void setDescription(LectureDescription description) {
        this.description = description;
    }

    public Set<LectureComment> getComments() {
        return comments;
    }
    public void setComments(Set<LectureComment> comments) {
        this.comments = comments;
    }

    public Video getVideo() {
        return video;
    }

    public void setVideo(Video video) {
        this.video = video;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }
    public boolean isDeleted() {
        return isDeleted;
    }

    public void setDeleted(boolean deleted) {
        isDeleted = deleted;
    }
    public Course getCourse() {
        return course;
    }

    public void setCourse(Course course) {
        this.course = course;
    }

    public String getAssignment(){
        StringBuilder sb = new StringBuilder();
        sb.append("\\assignments\\");
        sb.append(getId()).append(".docx");
        Path path = Paths.get(sb.toString());
        return path.toString();
    }

}
