package com.example.virtual_teacher.models;

import com.example.virtual_teacher.models.validators.ValidPassword;
import com.fasterxml.jackson.annotation.JsonIgnore;

import javax.persistence.*;
import javax.validation.constraints.Email;
import javax.validation.constraints.Pattern;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.time.LocalDateTime;
import java.util.Collection;
import java.util.List;
import java.util.Set;

@Entity
@Table(name = "users")
public class User {

    @JsonIgnore
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "user_id")
    private int id;

    @Column(name = "first_name")
    private String firstName;

    @Column(name = "last_name")
    private String lastName;

    @Column(name = "email")
    @Email(message = "Example: testemail@something.com", regexp = "[a-z0-9._%+-]+@[a-z0-9.-]+\\.[a-z]{2,3}",
            flags = Pattern.Flag.CASE_INSENSITIVE)
    private String email;

    @JsonIgnore
    @ValidPassword
    @Column(name = "password")
    private String password;

    @JsonIgnore
    @Column(name = "is_active")
    private boolean isActive;

    @JsonIgnore
    @Column(name = "is_deleted")
    private boolean isDeleted;

    @JsonIgnore
    @OneToOne
    @JoinColumn(name = "role_id")
    private UserRole role;

    @Column(name = "creation_date")
    private LocalDateTime date;

    @JsonIgnore
    @OneToOne
    @JoinColumn(name = "user_id")
    private MotivationalLetter motivationalLetter;

    public User() {
        date = LocalDateTime.now();
    }

    public String getFirstName() {
        return firstName;
    }

    public void setFirstName(String firstName) {
        this.firstName = firstName;
    }

    public String getLastName() {
        return lastName;
    }

    public void setLastName(String lastName) {
        this.lastName = lastName;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    public boolean isActive() {
        return isActive;
    }

    public void setActive(boolean active) {
        isActive = active;
    }

    public UserRole getRole() {
        return role;
    }

    public void setRole(UserRole role) {
        this.role = role;
    }

    public boolean isAdmin(){
        return getRole().getRole().equalsIgnoreCase("admin");
    }

    public boolean isTeacher(){
        return getRole().getRole().equalsIgnoreCase("teacher");
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public LocalDateTime getDate() {
        return date;
    }

    public void setDate(LocalDateTime date) {
        this.date = date;
    }

    public boolean isDeleted() {
        return isDeleted;
    }

    public void setDeleted(boolean deleted) {
        isDeleted = deleted;
    }

    public String getStatus(){
        if(isActive){
            return "Active";
        }
        return "Blocked";
    }

    public String getImage(){
        //check if image exists, if not return defaultImg.
        StringBuilder sb = new StringBuilder();
        StringBuilder defaultPath = new StringBuilder();
        defaultPath.append("\\userImages\\");
        StringBuilder fileName =  new StringBuilder();
        fileName.append(getEmail()).append(".jpg");
        Path path = Paths.get(String.valueOf(sb.append(Paths.get("").toAbsolutePath()).append("\\src\\main\\resources\\static\\userImages\\").append(fileName)));
        if(Files.exists(path)){
            return defaultPath.append(fileName).toString();
        }
        return  defaultPath.append("defaultImg.jpg").toString();
    }

    public MotivationalLetter getMotivationalLetter() {
        return motivationalLetter;
    }

    public void setMotivationalLetter(MotivationalLetter motivationalLetter) {
        this.motivationalLetter = motivationalLetter;
    }

    public String getFullName(){
        return String.format("%s %s",getFirstName(),getLastName());
    }
}