package com.example.virtual_teacher.helpers;

import com.example.virtual_teacher.exceptions.AuthenticationFailureException;
import com.example.virtual_teacher.exceptions.EntityNotFoundException;
import com.example.virtual_teacher.exceptions.InvalidEmailException;
import com.example.virtual_teacher.exceptions.UnauthorizedOperationException;
import com.example.virtual_teacher.models.User;
import com.example.virtual_teacher.services.contracts.UserService;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Component;
import org.springframework.web.server.ResponseStatusException;

import javax.servlet.http.HttpSession;

@Component
public class AuthenticationHelper {

    public static final String AUTHENTICATION_FAILURE_MESSAGE = "Wrong email or password.";
    public static final String AUTHENTICATION_HEADER_NAME = "Authentication";
    public static final String PASSWORD_HEADER_NAME = "Password";
    public static final String USER_IS_BLOCKED_MESSAGE= "You are blocked and cannot do anything about it!";
    public static final String USER_IS_DELETED_MESSAGE= "YOUR ACCOUNT HAS BEEN DELETED!";
    public static final String RESOURCE_REQUIRES_AUTHORIZATION = "The request for this resource requires authorization!";
    public static final String NO_USER_LOGGED_IN = "No user logged in.";

    public static String ERROR_MESSAGE;
    private final UserService userService;

    @Autowired
    public AuthenticationHelper(UserService userService) {
        this.userService = userService;
    }

    public User tryGetUser(HttpHeaders headers) {
        if (!headers.containsKey(AUTHENTICATION_HEADER_NAME) || !headers.containsKey(PASSWORD_HEADER_NAME)) {
            ERROR_MESSAGE = RESOURCE_REQUIRES_AUTHORIZATION;
            throw new ResponseStatusException(HttpStatus.UNAUTHORIZED,
                    RESOURCE_REQUIRES_AUTHORIZATION);
        }
        try {
            String email = headers.getFirst(AUTHENTICATION_HEADER_NAME);
            String password = headers.getFirst(PASSWORD_HEADER_NAME);
            User user = userService.getByEmail(email);
            if (!user.getPassword().equals(password)) {
                ERROR_MESSAGE = AUTHENTICATION_FAILURE_MESSAGE;
                throw new InvalidEmailException(AUTHENTICATION_FAILURE_MESSAGE);
            }

            if (!user.isActive()) {
                ERROR_MESSAGE = USER_IS_BLOCKED_MESSAGE;
                throw new UnauthorizedOperationException(USER_IS_BLOCKED_MESSAGE);
            }

            if(user.isDeleted()){
                ERROR_MESSAGE = USER_IS_DELETED_MESSAGE;
                throw new UnauthorizedOperationException(USER_IS_DELETED_MESSAGE);
            }
                return user;
        } catch (EntityNotFoundException e) {
            throw new ResponseStatusException(HttpStatus.UNAUTHORIZED, "Invalid Username!");
        }
    }
    public User tryGetUser(HttpSession session) {
        String currentUser = (String) session.getAttribute("currentUserEmail");

        if (currentUser == null) {
            ERROR_MESSAGE = NO_USER_LOGGED_IN;
            throw new AuthenticationFailureException(NO_USER_LOGGED_IN);
        }

        return userService.getByEmail(currentUser);
    }
    public User verifyAuthentication(String username, String password) {
        try {
            User user = userService.getByEmail(username);
            if (!user.getPassword().equalsIgnoreCase(password)) {
                ERROR_MESSAGE = AUTHENTICATION_FAILURE_MESSAGE;
                throw new AuthenticationFailureException(AUTHENTICATION_FAILURE_MESSAGE);
            }

            if(!user.isActive()){
                ERROR_MESSAGE = USER_IS_BLOCKED_MESSAGE;
                throw new AuthenticationFailureException(USER_IS_BLOCKED_MESSAGE);
            }

            if(user.isDeleted()){
                ERROR_MESSAGE = USER_IS_DELETED_MESSAGE;
                throw new AuthenticationFailureException(USER_IS_DELETED_MESSAGE);
            }
            return user;
        } catch (EntityNotFoundException e) {
            ERROR_MESSAGE = AUTHENTICATION_FAILURE_MESSAGE;
            throw new AuthenticationFailureException(AUTHENTICATION_FAILURE_MESSAGE);
        }
    }

}
