package com.example.virtual_teacher.models;

import com.fasterxml.jackson.annotation.JsonIgnore;

import javax.persistence.*;

@Entity
@Table(name = "videos")
public class Video {

    @Column(name = "video_url")
    private String videoUrl;

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "video_id")
    private int videoId;

    @JsonIgnore
    @Column(name = "is_deleted")
    private boolean isDeleted;

    public boolean isDeleted() {
        return isDeleted;
    }

    public void setDeleted(boolean deleted) {
        isDeleted = deleted;
    }

    public Video() {
    }

    public Video(String videoUrl) {
    }

    public String getVideoUrl() {
        return videoUrl;
    }

    public void setVideoUrl(String videoUrl) {
        StringBuilder sb = new StringBuilder();
        sb.append("https://www.youtube.com/embed/");
        sb.append(videoUrl);
        this.videoUrl = sb.toString();
    }

    public int getVideoId() {
        return videoId;
    }

    public void setVideoId(int videoId) {
        this.videoId = videoId;
    }
}
