package com.karolwrona.usermanagement.controller;

import com.karolwrona.usermanagement.model.Role;
import com.karolwrona.usermanagement.model.User;
import com.karolwrona.usermanagement.service.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import jakarta.validation.Valid;
import org.springframework.web.server.ResponseStatusException;

import java.util.List;
import java.util.Set;

@RestController
@RequestMapping("/api/auth/users")
public class UserController {

    @Autowired
    private UserService userService;

    @GetMapping
    public List<User> getAllUsers() {
        return userService.findAll();
    }

    @PostMapping
    public ResponseEntity<?> createUser(@Valid @RequestBody User user) {
        User createUser = userService.save(user);
        return ResponseEntity.ok(createUser);
    }

    @DeleteMapping("/{id}")
    public void deleteUser(@PathVariable Long id) {
        userService.deleteById(id);
    }

    @PutMapping("/{userId}/roles/{roleId}")
    public User assignRoleToUser(@PathVariable Long userId, @PathVariable Long roleId) {
        return userService.assignRoleToUser(userId, roleId);
    }

    @DeleteMapping("/{userId}/roles/{roleId}")
    public User removeRoleFromUser(@PathVariable Long userId, @PathVariable Long roleId) {
        return userService.removeRoleFromUser(userId, roleId);
    }

    @GetMapping("/roles/{roleName}")
    public ResponseEntity<List<User>> getUserByRole(@PathVariable String roleName) {
        List<User> users = userService.findUsersByRole(roleName);
        return ResponseEntity.ok(users);
    }

    @GetMapping("/{id}/roles")
    public ResponseEntity<Set<Role>> getRolesForUser(@PathVariable Long id) {
        User user = userService.findById(id)
                .orElseThrow(() -> new RuntimeException("User not found"));
        return ResponseEntity.ok(user.getRoles());
    }

    @GetMapping(params = "username")
    public ResponseEntity<User> getUserByUsername(@RequestParam String username) {
        User user = userService.findByUsername(username)
                .orElseThrow(() -> new RuntimeException("User not found with username: " + username));
        return ResponseEntity.ok(user);
    }

    @GetMapping("/{id}")
    public ResponseEntity<User> getUserById(@PathVariable Long id) {
        User user = userService.findById(id)
                .orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND, "User not found with ID: " + id));
        return ResponseEntity.ok(user);
    }
}