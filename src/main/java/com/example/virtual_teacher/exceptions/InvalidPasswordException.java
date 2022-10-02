package com.example.virtual_teacher.exceptions;

public class InvalidPasswordException extends RuntimeException{
    public InvalidPasswordException(String message) {
        String.format(message);
    }
}
