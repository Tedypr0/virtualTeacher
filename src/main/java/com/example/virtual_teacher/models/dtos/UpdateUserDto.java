package com.example.virtual_teacher.models.dtos;



import javax.validation.constraints.Size;
import java.io.File;

public class UpdateUserDto {

    @Size(min = 4, max = 32, message = "First name must be between 4 and 32 symbols")
    private String firstName;

    @Size(min = 4, max = 32, message = "Last name must be between 4 and 32 symbols")
    private String lastName;

    @Size(min = 4, max = 32, message = "Password must be between 4 and 32 symbols")
    private String password;

    private int role;

    private boolean isActive;

    private File imgUrl;

    public int getRole() {
        return role;
    }

    public void setRole(int role) {
        this.role = role;
    }

    public boolean isActive() {
        return isActive;
    }

    public void setIsActive(boolean active) {
        isActive = active;
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

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    public void setActive(boolean active) {
        isActive = active;
    }

    public File getImgUrl() {
        return imgUrl;
    }

    public void setImgUrl(File imgUrl) {
        this.imgUrl = imgUrl;
    }
}
