package com.example.virtual_teacher.exceptions;

public class AuthenticationFailureException extends RuntimeException{
    public AuthenticationFailureException(String message) {
        super(message);
    }
}
