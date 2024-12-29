package com.karolwrona.usermanagement.DTOs;

import java.util.Set;

public class RoleDTO {

    private Long id;
    private String name;
    private Set<String> users; // Nazwy użytkowników

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public Set<String> getUsers() {
        return users;
    }

    public void setUsers(Set<String> users) {
        this.users = users;
    }
}