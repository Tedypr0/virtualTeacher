package com.example.virtual_teacher.services.mappers;

import com.example.virtual_teacher.models.MotivationalLetter;
import com.example.virtual_teacher.models.User;
import com.example.virtual_teacher.models.UserRole;
import com.example.virtual_teacher.models.dtos.RegisterDto;
import com.example.virtual_teacher.models.dtos.UpdateUserDto;
import com.example.virtual_teacher.services.contracts.RoleService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;


@Component
public class UserMapper {

    private final RoleService roleService;

    @Autowired
    public UserMapper(RoleService roleService) {
        this.roleService = roleService;
    }

    public User dtoToObj(RegisterDto userDto){
        User user = new User();
        user.setFirstName(userDto.getFirstName());
        user.setLastName(userDto.getLastName());
        user.setPassword(userDto.getPassword());
        user.setEmail(userDto.getEmail());
        user.setActive(true);
        UserRole role = roleService.getByRoleName("Student");
        user.setRole(role);
        return user;
    }

    public User userDtoToObj(UpdateUserDto updateUserDto, User originalUser){
        originalUser.setFirstName(updateUserDto.getFirstName());
        originalUser.setLastName(updateUserDto.getLastName());
        originalUser.setPassword(updateUserDto.getPassword());
        return originalUser;
    }

    public UpdateUserDto objToUserDto(UpdateUserDto updateUserDto, User originalUser){
        updateUserDto.setFirstName(originalUser.getFirstName());
        updateUserDto.setLastName(originalUser.getLastName());
        updateUserDto.setPassword(originalUser.getPassword());
        return updateUserDto;
    }

    public User updateAdminDtoToUser(UpdateUserDto updateUserDto, User originalUser) {
        originalUser = userDtoToObj(updateUserDto, originalUser);
        UserRole role = roleService.getById(updateUserDto.getRole());
        originalUser.setRole(role);
        return originalUser;
    }

    public UpdateUserDto updateAdminUserToDto(UpdateUserDto updateUserDto, User originalUser) {
        updateUserDto = objToUserDto(updateUserDto, originalUser);
        updateUserDto.setRole(originalUser.getRole().getId());
        updateUserDto.setIsActive(updateUserDto.isActive());
        return updateUserDto;
    }



    public User updateUserRoleToTeacher(User originalUser, String decision){
        if(decision.equalsIgnoreCase("accept")) {
            UserRole role = roleService.getByRoleName("Teacher");
            originalUser.setRole(role);
        }
        return originalUser;
    }

    public MotivationalLetter motivationalLetterFieldToObj(User authUser, String letter){
        MotivationalLetter motivationalLetter = new MotivationalLetter();
        motivationalLetter.setMotivationalLetter(letter);
        motivationalLetter.setUserId(authUser.getId());
        return motivationalLetter;
    }
}

