package com.karolwrona.usermanagement.service;

import com.karolwrona.usermanagement.DTOs.UserDTO;
import com.karolwrona.usermanagement.model.Role;
import com.karolwrona.usermanagement.model.User;
import com.karolwrona.usermanagement.repository.RoleRepository;
import com.karolwrona.usermanagement.repository.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@Service
public class UserService {

    @Autowired
    private UserRepository userRepository;

    @Autowired
    private RoleRepository roleRepository;

    /**
     * Get all users
     */
    public List<User> findAll() {
        return userRepository.findAll();
    }

    /**
     * Get users with exact role
     */
    public List<User> findUsersByRole(String roleName) {
        return userRepository.findUsersByRoleName(roleName);
    }

    /**
     * Add new user
     */
    public User save(User user) {
        userRepository.findByUsername(user.getUsername())
                .ifPresent(existingUser -> {
                    throw new IllegalArgumentException("User with this username already exists: " + user.getUsername());
                });
        return userRepository.save(user);
    }

    public UserDTO mapToUserDTO(User user) {
        UserDTO userDTO = new UserDTO();
        userDTO.setId(user.getId());
        userDTO.setUsername(user.getUsername());
        userDTO.setRoles(user.getRoles().stream().map(Role::getName).collect(Collectors.toSet()));
        return userDTO;
    }

    /**
     * Delete user by ID
     */
    public void deleteById(Long id) {
        if (!userRepository.existsById(id)) {
            throw new IllegalArgumentException("User with ID " + id + " does not exist");
        }
        userRepository.deleteById(id);
    }

    /**
     * Get user by ID
     */
    public Optional<User> findById(Long id) {
        return userRepository.findById(id);
    }

    /**
     * Add user role
     */
    @Transactional
    public User assignRoleToUser(Long userId, Long roleId) {
        User user = userRepository.findById(userId)
                .orElseThrow(() -> new IllegalArgumentException("User not found with ID: " + userId));
        Role role = roleRepository.findById(roleId)
                .orElseThrow(() -> new IllegalArgumentException("Role not found with ID: " + roleId));

        if (!user.getRoles().add(role)) {
            throw new IllegalStateException("User already has this role assigned");
        }

        return userRepository.save(user);
    }

    /**
     * Delete user role
     */
    @Transactional
    public User removeRoleFromUser(Long userId, Long roleId) {
        User user = userRepository.findById(userId)
                .orElseThrow(() -> new IllegalArgumentException("User not found with ID: " + userId));
        Role role = roleRepository.findById(roleId)
                .orElseThrow(() -> new IllegalArgumentException("Role not found with ID: " + roleId));

        if (!user.getRoles().remove(role)) {
            throw new IllegalStateException("User does not have this role assigned");
        }

        return userRepository.save(user);
    }

    /**
     * Find user by username
     */
    public Optional<User> findByUsername(String username) {
        return userRepository.findByUsername(username);
    }
}