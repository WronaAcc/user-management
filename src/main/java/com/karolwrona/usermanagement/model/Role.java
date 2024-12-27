package com.karolwrona.usermanagement.model;

import com.fasterxml.jackson.annotation.JsonBackReference;
import jakarta.persistence.*;
import jakarta.validation.constraints.Pattern;
import org.apache.logging.log4j.core.config.plugins.validation.constraints.NotBlank;

import java.util.HashSet;
import java.util.Set;

@Entity
@Table(name = "roles")
public class Role {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(nullable = false, unique = true)
    @NotBlank(message = "Role name cannot be empty")
    @Pattern(regexp = "^ROLE_.*", message = "Role name must start with 'ROLE_'")
    private String name;

    @JsonBackReference
    @ManyToMany(mappedBy = "roles", cascade = {CascadeType.MERGE, CascadeType.REMOVE})
    private Set<User> users = new HashSet<>();

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

    public Set<User> getUsers() {
        return users;
    }

    public void setUsers(Set<User> users) {
        this.users = users;
    }

    public void addUser(User user) {
        if (!this.users.contains(user)) {
            this.users.add(user);
            user.getRoles().add(this);
        }
    }

    public void removeUser(User user) {
        this.users.remove(user);
        user.getRoles().remove(this);
    }
}