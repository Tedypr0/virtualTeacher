package com.example.virtual_teacher.models;


import javax.persistence.*;

@Entity
@Table(name = "teacher_applications")
public class MotivationalLetter {

    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id")
    private int id;

    @Id
    @Column(name = "user_id")
    private int userId;

    @Column(name = "motivational_letter")
    private String motivationalLetter;

    public MotivationalLetter() {
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public int getUserId() {
        return userId;
    }

    public void setUserId(int userId) {
        this.userId = userId;
    }

    public String getMotivationalLetter() {
        return motivationalLetter;
    }

    public void setMotivationalLetter(String motivationalLetter) {
        this.motivationalLetter = motivationalLetter;
    }
}
