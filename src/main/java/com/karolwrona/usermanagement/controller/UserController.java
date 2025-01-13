package com.karolwrona.usermanagement.controller;

import com.karolwrona.usermanagement.DTOs.UserDTO;
import com.karolwrona.usermanagement.service.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;
import jakarta.validation.Valid;

import java.util.List;
import java.util.Set;

@RestController
@RequestMapping("/api/auth/users")
public class UserController {

    @Autowired
    private UserService userService;

    /**
     * Get all users
     */
    @GetMapping
    @PreAuthorize("hasRole('ADMIN')")
    public List<UserDTO> getAllUsers() {
        return userService.findAll();
    }

    /**
     * Create a new user
     */
    @PostMapping
    @PreAuthorize("hasRole('ADMIN')")
    public ResponseEntity<UserDTO> createUser(@Valid @RequestBody UserDTO userDTO) {
        UserDTO createdUser = userService.save(userDTO);
        return ResponseEntity.ok(createdUser);
    }

    /**
     * Delete user by ID
     */
    @DeleteMapping("/{id}")
    @PreAuthorize("hasRole('ADMIN')")
    public ResponseEntity<Void> deleteUser(@PathVariable Long id) {
        userService.deleteById(id);
        return ResponseEntity.noContent().build();
    }

    /**
     * Assign a role to user
     */
    @PutMapping("/{userId}/roles/{roleId}")
    @PreAuthorize("hasRole('ADMIN')")
    public ResponseEntity<UserDTO> assignRoleToUser(@PathVariable Long userId, @PathVariable Long roleId) {
        UserDTO updatedUser = userService.assignRoleToUser(userId, roleId);
        return ResponseEntity.ok(updatedUser);
    }

    /**
     * Remove a role from user
     */
    @DeleteMapping("/{userId}/roles/{roleId}")
    @PreAuthorize("hasRole('ADMIN')")
    public ResponseEntity<UserDTO> removeRoleFromUser(@PathVariable Long userId, @PathVariable Long roleId) {
        UserDTO updatedUser = userService.removeRoleFromUser(userId, roleId);
        return ResponseEntity.ok(updatedUser);
    }

    /**
     * Get users by role name
     */
    @GetMapping("/roles/{roleName}")
    @PreAuthorize("hasRole('ADMIN')")
    public ResponseEntity<List<UserDTO>> getUsersByRole(@PathVariable String roleName) {
        List<UserDTO> users = userService.findUsersByRole(roleName);
        return ResponseEntity.ok(users);
    }

    /**
     * Get roles for a specific user
     */
    @GetMapping("/{id}/roles")
    @PreAuthorize("hasRole('ADMIN')")
    public ResponseEntity<Set<String>> getRolesForUser(@PathVariable Long id) {
        UserDTO userDTO = userService.findById(id)
                .orElseThrow(() -> new RuntimeException("User not found"));
        return ResponseEntity.ok(userDTO.getRoles());
    }

    /**
     * Get user by username
     */
    @GetMapping(params = "username")
    @PreAuthorize("hasRole('ADMIN')")
    public ResponseEntity<UserDTO> getUserByUsername(@RequestParam String username) {
        UserDTO userDTO = userService.findByUsername(username)
                .orElseThrow(() -> new RuntimeException("User not found with username: " + username));
        return ResponseEntity.ok(userDTO);
    }

    /**
     * Get user by ID
     */
    @GetMapping("/{id}")
    @PreAuthorize("hasRole('ADMIN')")
    public ResponseEntity<UserDTO> getUserById(@PathVariable Long id) {
        UserDTO userDTO = userService.findById(id)
                .orElseThrow(() -> new RuntimeException("User not found"));
        return ResponseEntity.ok(userDTO);
    }
}