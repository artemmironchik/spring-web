package com.example.springweb.service;

import com.example.springweb.entity.Role;
import com.example.springweb.entity.User;
import com.example.springweb.repository.RoleRepository;
import org.springframework.stereotype.Service;

import java.util.Optional;

@Service
public class RoleService {

    private final RoleRepository roleRepository;

    public RoleService(RoleRepository rolesRepository) {
        this.roleRepository = rolesRepository;
    }

    public void setUserRole(User user) {
        this.setRole(user, "ROLE_USER");
    }

    public void setRole(User user, String roleName) {
        Role role = saveRole(roleName);
        user.setRole(role);
    }

    public Role saveRole(String name) {
        Optional<Role> userRole = roleRepository.findByName(name);
        Role role;
        role = userRole.orElseGet(() -> roleRepository.save(new Role(name)));
        return role;
    }
}
