package com.example.virtual_teacher.exceptions;

public class InvalidEmailException extends RuntimeException {
    public InvalidEmailException(String message, String username) {
        String.format(message, username);
    }
    public InvalidEmailException(String message) {
        String.format(message);
    }
}
