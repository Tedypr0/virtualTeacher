package com.example.virtual_teacher.models.dtos;


import com.example.virtual_teacher.models.validators.ValidPassword;

import javax.validation.constraints.Email;
import javax.validation.constraints.Pattern;
import javax.validation.constraints.Size;

public class RegisterDto {


    @Size(min = 8, max = 50, message = "Passwords do not match")
    @ValidPassword
    private String password;

    @Size(min = 8, max = 50, message = "Passwords do not match")
    @ValidPassword
    private String passwordConfirm;

    @Size(min = 2, max = 20, message = "First name must be between 2 and 20 symbols")
    private String firstName;

    @Size(min = 2, max = 20, message = "Last name must be between 2 and 20 symbols")
    private String lastName;

    @Email(message = "Example: testemail@something.com", regexp = "[a-z0-9._%+-]+@[a-z0-9.-]+\\.[a-z]{2,3}",
            flags = Pattern.Flag.CASE_INSENSITIVE)
    @Size(min = 5, max = 100, message = "Email must be between 5 and 100 symbols")
    private String email;

    public RegisterDto() {}

    public String getPasswordConfirm() {
        return passwordConfirm;
    }

    public void setPasswordConfirm(String passwordConfirm) {
        this.passwordConfirm = passwordConfirm;
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
}
