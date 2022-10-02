package com.example.virtual_teacher.exceptions;

public class InvalidUsernameOrPasswordException extends RuntimeException {
    public InvalidUsernameOrPasswordException(String message, String username) {
        String.format(message, username);
    }
    public InvalidUsernameOrPasswordException(String message) {
        String.format(message);
    }
}
