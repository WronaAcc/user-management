package com.karolwrona.usermanagement.service;

import com.karolwrona.usermanagement.DTOs.RoleDTO;
import com.karolwrona.usermanagement.model.Role;
import com.karolwrona.usermanagement.model.User;
import com.karolwrona.usermanagement.repository.RoleRepository;
import com.karolwrona.usermanagement.repository.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;
import java.util.Set;
import java.util.stream.Collectors;

@Service
public class RoleService {

    @Autowired
    private RoleRepository roleRepository;

    @Autowired
    private UserRepository userRepository;

    public List<RoleDTO> findAll() {
        return roleRepository.findAll().stream()
                .map(this::mapToRoleDTO)
                .collect(Collectors.toList());
    }

    public RoleDTO save(RoleDTO roleDTO) {
        roleRepository.findByName(roleDTO.getName())
                .ifPresent(existingRole -> {
                    throw new IllegalArgumentException("Role with this name already exists: " + roleDTO.getName());
                });

        Role role = mapToEntity(roleDTO);
        role = roleRepository.save(role);
        return mapToRoleDTO(role);
    }

    public void deleteById(Long id) {
        if (!roleRepository.existsById(id)) {
            throw new IllegalArgumentException("Role with ID " + id + " does not exist");
        }
        roleRepository.deleteById(id);
    }

    public Optional<RoleDTO> findById(Long id) {
        return roleRepository.findById(id).map(this::mapToRoleDTO);
    }

    public Optional<Role> findEntityByName(String name) {
        return roleRepository.findByName(name);
    }

    public RoleDTO mapToRoleDTO(Role role) {
        RoleDTO roleDTO = new RoleDTO();
        roleDTO.setId(role.getId());
        roleDTO.setName(role.getName());

        // Mapowanie Set<User> na Set<String> (nazwy użytkowników)
        roleDTO.setUsers(role.getUsers().stream()
                .map(User::getUsername)
                .collect(Collectors.toSet()));

        return roleDTO;
    }

    public Role mapToEntity(RoleDTO roleDTO) {
        Role role = new Role();
        role.setId(roleDTO.getId());
        role.setName(roleDTO.getName());

        // Mapowanie Set<String> na Set<User>
        if (roleDTO.getUsers() != null) {
            Set<User> users = roleDTO.getUsers().stream()
                    .map(username -> userRepository.findByUsername(username)
                            .orElseThrow(() -> new IllegalArgumentException("User not found: " + username)))
                    .collect(Collectors.toSet());
            role.setUsers(users);
        }

        return role;
    }
}