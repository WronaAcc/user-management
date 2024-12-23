package com.karolwrona.usermanagement.controller;

import com.karolwrona.usermanagement.model.Role;
import com.karolwrona.usermanagement.model.User;
import com.karolwrona.usermanagement.service.RoleService;
import com.karolwrona.usermanagement.service.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/public") // Publiczne endpointy
public class AuthController {

    @Autowired
    private UserService userService;

    @Autowired
    private PasswordEncoder passwordEncoder;
    @Autowired
    private RoleService roleService;

    @PostMapping("/register")
    public ResponseEntity<?> registerUser(@RequestBody User user) {
        if (user.getPassword() == null || user.getPassword().isEmpty()) {
            return ResponseEntity.badRequest().body("Password cannot be null or empty");
        }

        Role userRole = roleService.findByName("ROLE_USER")
                .orElseThrow(() -> new RuntimeException("Role not found"));

        user.addRole(userRole);

        user.setPassword(passwordEncoder.encode(user.getPassword()));
        userService.save(user);
        return ResponseEntity.ok("User registered successfully");
    }
}