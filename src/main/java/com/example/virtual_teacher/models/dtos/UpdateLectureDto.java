package com.example.virtual_teacher.models.dtos;

import javax.validation.constraints.NotNull;
import javax.validation.constraints.Size;
import java.io.File;

public class UpdateLectureDto {

    @NotNull
    @Size(min = 5, max = 50, message = "Lecture title must be between 5 and 50 symbols")
    private String title;

    private String description;

    @NotNull
    @Size(min = 1, max = 500, message = "Please add video URL")
    private String videoUrl;

    public UpdateLectureDto() {
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public String getVideoUrl() {
        return videoUrl.substring(videoUrl.lastIndexOf("/")+1);
    }

    public void setVideoUrl(String videoUrl) {
        this.videoUrl = videoUrl;
    }
}
