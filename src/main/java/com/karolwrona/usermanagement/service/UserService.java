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
import java.util.Set;
import java.util.stream.Collectors;

@Service
public class UserService {

    private final UserRepository userRepository;
    private final RoleRepository roleRepository;

    @Autowired
    public UserService(UserRepository userRepository, RoleRepository roleRepository) {
        this.userRepository = userRepository;
        this.roleRepository = roleRepository;
    }

    /**
     * Get all users
     */
    public List<UserDTO> findAll() {
        return userRepository.findAll().stream()
                .map(this::mapToUserDTO)
                .collect(Collectors.toList());
    }

    /**
     * Get users with exact role
     */
    public List<UserDTO> findUsersByRole(String roleName) {
        return userRepository.findUsersByRoleName(roleName).stream()
                .map(this::mapToUserDTO)
                .collect(Collectors.toList());
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
    public Optional<UserDTO> findById(Long id) {
        return userRepository.findById(id).map(this::mapToUserDTO);
    }

    /**
     * Add user role
     */
    @Transactional
    public UserDTO assignRoleToUser(Long userId, Long roleId) {
        User user = userRepository.findById(userId)
                .orElseThrow(() -> new IllegalArgumentException("User not found with ID: " + userId));
        Role role = roleRepository.findById(roleId)
                .orElseThrow(() -> new IllegalArgumentException("Role not found with ID: " + roleId));

        if (!user.getRoles().add(role)) {
            throw new IllegalStateException("User already has this role assigned");
        }

        user = userRepository.save(user);
        return mapToUserDTO(user);
    }

    /**
     * Delete user role
     */
    @Transactional
    public UserDTO removeRoleFromUser(Long userId, Long roleId) {
        User user = userRepository.findById(userId)
                .orElseThrow(() -> new IllegalArgumentException("User not found with ID: " + userId));
        Role role = roleRepository.findById(roleId)
                .orElseThrow(() -> new IllegalArgumentException("Role not found with ID: " + roleId));

        if (!user.getRoles().remove(role)) {
            throw new IllegalStateException("User does not have this role assigned");
        }

        user = userRepository.save(user);
        return mapToUserDTO(user);
    }

    /**
     * Find user by username
     */
    public Optional<UserDTO> findByUsername(String username) {
        return userRepository.findByUsername(username).map(this::mapToUserDTO);
    }

    /**
     * Map User to UserDTO
     */
    public UserDTO mapToUserDTO(User user) {
        UserDTO userDTO = new UserDTO();
        userDTO.setId(user.getId());
        userDTO.setUsername(user.getUsername());
        userDTO.setRoles(user.getRoles().stream().map(Role::getName).collect(Collectors.toSet()));
        return userDTO;
    }

    /**
     * Map UserDTO to User
     */
    public User mapToEntity(UserDTO userDTO) {
        User user = new User();
        user.setId(userDTO.getId());
        user.setUsername(userDTO.getUsername());

        if (userDTO.getRoles() != null) {
            Set<Role> roles = userDTO.getRoles().stream()
                    .map(roleName -> roleRepository.findByName(roleName)
                            .orElseThrow(() -> new IllegalArgumentException("Role not found: " + roleName)))
                    .collect(Collectors.toSet());
            user.setRoles(roles);
        }
        return user;
    }

    /**
     *Add user
     * */
     public UserDTO save(UserDTO userDTO) {
        userRepository.findByUsername(userDTO.getUsername())
                .ifPresent(existingUser -> {
                    throw new IllegalArgumentException("User with this username already exists: " + userDTO.getUsername());
                });

        userRepository.findByEmail(userDTO.getEmail())
                .ifPresent(existingUser -> {
                    throw new IllegalArgumentException("User with this email already exists: " + userDTO.getEmail());
                });

        User user = mapToEntity(userDTO);
        user = userRepository.save(user);
        return mapToUserDTO(user);
    }
}