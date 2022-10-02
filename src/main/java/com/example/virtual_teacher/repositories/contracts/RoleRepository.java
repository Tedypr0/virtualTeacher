package com.example.virtual_teacher.repositories.contracts;

import com.example.virtual_teacher.models.UserRole;

import java.util.List;

public interface RoleRepository {

    List<UserRole> getAll();

    UserRole getById(int id);

    UserRole getByRoleName(String name);

    UserRole create(UserRole userRole);

    UserRole update(UserRole userRole);

    UserRole delete(int id);
}
