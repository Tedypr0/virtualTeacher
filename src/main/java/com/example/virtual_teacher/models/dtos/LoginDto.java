package com.example.virtual_teacher.models.dtos;

import com.example.virtual_teacher.models.validators.ValidPassword;

import javax.validation.constraints.Email;
import javax.validation.constraints.Pattern;

public class LoginDto {
    @Email(message = "Example: testemail@something.com", regexp = "[a-z0-9._%+-]+@[a-z0-9.-]+\\.[a-z]{2,3}",
            flags = Pattern.Flag.CASE_INSENSITIVE)
    private String email;

    private String password;

    public LoginDto() {}

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
