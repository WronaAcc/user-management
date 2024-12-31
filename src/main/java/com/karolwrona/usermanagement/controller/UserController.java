package com.karolwrona.usermanagement.controller;

import com.karolwrona.usermanagement.DTOs.UserDTO;
import com.karolwrona.usermanagement.service.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import jakarta.validation.Valid;

import java.util.List;
import java.util.Set;

@RestController
@RequestMapping("/api/auth")
public class UserController {

    @Autowired
    private UserService userService;

    /**
     * Get all users
     */
    @GetMapping("/users")
    public List<UserDTO> getAllUsers() {
        return userService.findAll();
    }

    /**
     * Register a new user
     */
    @PostMapping("/register")
    public ResponseEntity<String> registerUser(@Valid @RequestBody UserDTO userDTO) {
        userService.register(userDTO);
        return ResponseEntity.ok("Registration successful. Please check your email to activate your account.");
    }

    /**
     * Activate user account
     */
    @GetMapping("/activate")
    public ResponseEntity<String> activateAccount(@RequestParam String token) {
        userService.activateUser(token);
        return ResponseEntity.ok("Account activated successfully.");
    }

    /**
     * Delete user by ID
     */
    @DeleteMapping("/users/{id}")
    public ResponseEntity<Void> deleteUser(@PathVariable Long id) {
        userService.deleteById(id);
        return ResponseEntity.noContent().build();
    }

    /**
     * Assign a role to user
     */
    @PutMapping("/users/{userId}/roles/{roleId}")
    public ResponseEntity<UserDTO> assignRoleToUser(@PathVariable Long userId, @PathVariable Long roleId) {
        UserDTO updatedUser = userService.assignRoleToUser(userId, roleId);
        return ResponseEntity.ok(updatedUser);
    }

    /**
     * Remove a role from user
     */
    @DeleteMapping("/users/{userId}/roles/{roleId}")
    public ResponseEntity<UserDTO> removeRoleFromUser(@PathVariable Long userId, @PathVariable Long roleId) {
        UserDTO updatedUser = userService.removeRoleFromUser(userId, roleId);
        return ResponseEntity.ok(updatedUser);
    }

    /**
     * Get users by role name
     */
    @GetMapping("/users/roles/{roleName}")
    public ResponseEntity<List<UserDTO>> getUsersByRole(@PathVariable String roleName) {
        List<UserDTO> users = userService.findUsersByRole(roleName);
        return ResponseEntity.ok(users);
    }

    /**
     * Get roles for a specific user
     */
    @GetMapping("/users/{id}/roles")
    public ResponseEntity<Set<String>> getRolesForUser(@PathVariable Long id) {
        UserDTO userDTO = userService.findById(id)
                .orElseThrow(() -> new RuntimeException("User not found"));
        return ResponseEntity.ok(userDTO.getRoles());
    }

    /**
     * Get user by username
     */
    @GetMapping("/users")
    public ResponseEntity<UserDTO> getUserByUsername(@RequestParam String username) {
        UserDTO userDTO = userService.findByUsername(username)
                .orElseThrow(() -> new RuntimeException("User not found with username: " + username));
        return ResponseEntity.ok(userDTO);
    }

    /**
     * Get user by ID
     */
    @GetMapping("/users/{id}")
    public ResponseEntity<UserDTO> getUserById(@PathVariable Long id) {
        UserDTO userDTO = userService.findById(id)
                .orElseThrow(() -> new RuntimeException("User not found"));
        return ResponseEntity.ok(userDTO);
    }
}