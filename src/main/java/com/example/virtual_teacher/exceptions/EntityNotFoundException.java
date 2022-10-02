package com.example.virtual_teacher.exceptions;

public class EntityNotFoundException extends RuntimeException {

    public EntityNotFoundException(String type, int id) {
        this(type, "id", String.valueOf(id));
    }

    public EntityNotFoundException(String type, String attribute, String value) {
        super(String.format("%s with %s %s not found.", type, attribute, value));
    }

    public EntityNotFoundException(String type, int userId, int postId) {
        super(String.format("%s for user with id %d and post with id %d was not found.", type, userId, postId));
    }

    public EntityNotFoundException(String type, int postId, String action) {
        super(String.format("%s with id %d does not have any %s", type, postId, action));
    }

    public EntityNotFoundException(String type, int postId, String type2, int commentId) {
        super(String.format("%s with id %d or %s with id %d does not exist.", type, postId, type2, commentId));
    }

    public EntityNotFoundException(String type, String attribute) {
        super(String.format("%s %s.", type, attribute));
    }
}
