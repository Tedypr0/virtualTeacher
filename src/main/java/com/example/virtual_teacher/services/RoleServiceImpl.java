package com.example.virtual_teacher.services;

import com.example.virtual_teacher.models.UserRole;
import com.example.virtual_teacher.repositories.contracts.RoleRepository;
import com.example.virtual_teacher.services.contracts.RoleService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class RoleServiceImpl implements RoleService {

    private final RoleRepository roleRepository;

    @Autowired
    public RoleServiceImpl(RoleRepository roleRepository) {
        this.roleRepository = roleRepository;
    }


    @Override
    public List<UserRole> getAll() {
        return roleRepository.getAll();
    }

    @Override
    public UserRole getById(int id) {
        return roleRepository.getById(id);
    }

    @Override
    public UserRole getByRoleName(String name) {
        return roleRepository.getByRoleName(name);
    }

    @Override
    public UserRole create(UserRole userRole) {
        return roleRepository.create(userRole);
    }

    @Override
    public UserRole update(UserRole userRole) {
        return roleRepository.update(userRole);
    }

    @Override
    public UserRole delete(int id) {
        return roleRepository.delete(id);
    }
}
