package com.example.virtual_teacher.models;

import com.fasterxml.jackson.annotation.JsonIgnore;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.Table;

@Entity
@Table(name = "assignments")
public class Assignment {

    @Column(name = "assignment_url")
    private String assignmentUrl;

    @Id
    @Column(name = "assignment_id")
    private int assignmentId;

    @JsonIgnore
    @Column(name = "is_deleted")
    private boolean isDeleted;

    public boolean isDeleted() {
        return isDeleted;
    }

    public void setDeleted(boolean deleted) {
        isDeleted = deleted;
    }

    public Assignment() {
    }

    public String getAssignmentUrl() {
        return assignmentUrl;
    }

    public void setAssignmentUrl(String assignmentUrl) {
        this.assignmentUrl = assignmentUrl;
    }

    public int getAssignmentId() {
        return assignmentId;
    }

    public void setAssignmentId(int assignmentId) {
        this.assignmentId = assignmentId;
    }
}
