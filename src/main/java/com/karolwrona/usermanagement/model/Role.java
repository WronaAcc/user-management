package com.karolwrona.usermanagement.model;

import jakarta.persistence.*;
import jakarta.validation.constraints.NotBlank;
import lombok.*;

import java.util.HashSet;
import java.util.Set;

@Entity
@Table(name = "roles")
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class Role {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(nullable = false, unique = true)
    @NotBlank(message = "Role name cannot be empty")
    private String name;

    @ManyToMany(mappedBy = "roles", cascade = {CascadeType.MERGE, CascadeType.PERSIST})
    @ToString.Exclude
    private Set<User> users = new HashSet<>();
}