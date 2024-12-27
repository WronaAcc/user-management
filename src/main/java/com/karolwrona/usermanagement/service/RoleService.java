package com.karolwrona.usermanagement.service;

import com.karolwrona.usermanagement.model.Role;
import com.karolwrona.usermanagement.repository.RoleRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
public class RoleService {

    @Autowired
    private RoleRepository roleRepository;

    public List<Role> findAll() {
        return roleRepository.findAll();
    }

    public Optional<Role> findByName(String name) {
        return roleRepository.findByName(name);
    }

    public Role save(Role role) {
        roleRepository.findByName(role.getName())
                .ifPresent(existingRole -> {
                    throw new IllegalArgumentException("Role with this name already exists: " + role.getName());
                });
        return roleRepository.save(role);
    }

    public void deleteById(Long id) {
        if (!roleRepository.existsById(id)) {
            throw new IllegalArgumentException("Role with ID " + id + " does not exist");
        }
        roleRepository.deleteById(id);
    }
}