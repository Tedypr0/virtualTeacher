package com.example.virtual_teacher.models.dtos;

import org.springframework.web.multipart.MultipartFile;

import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;

public class NewLectureDto {

    @NotNull
    @Size(min = 5, max = 50, message = "Lecture title must be between 5 and 50 symbols")
    private String title;
    @NotNull
    @Size(min = 1, max = 500, message = "Please add video URL")
    private String videoUrl;

    public NewLectureDto() {
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getVideoUrl() {
        return videoUrl;
    }

    public void setVideoUrl(String videoUrl) {
        this.videoUrl = videoUrl;
    }
}
