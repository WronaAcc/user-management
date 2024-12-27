package com.karolwrona.usermanagement.controller;

import com.karolwrona.usermanagement.model.Role;
import com.karolwrona.usermanagement.service.RoleService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;
import jakarta.validation.Valid;
import org.springframework.http.ResponseEntity;

import java.util.List;

@RestController
@RequestMapping("/api/auth/roles")
public class RoleController {

    @Autowired
    private RoleService roleService;

    @GetMapping
    public List<Role> getAllRoles() {
        return roleService.findAll();
    }

    @PostMapping
    public ResponseEntity<?> createRole(@Valid @RequestBody Role role) {
        Role createdRole = roleService.save(role);
        return ResponseEntity.ok(createdRole);
    }

    @DeleteMapping("/{id}")
    public void deleteRole(@PathVariable Long id) {
        roleService.deleteById(id);
    }
}