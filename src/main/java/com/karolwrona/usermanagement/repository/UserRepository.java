package com.karolwrona.usermanagement.repository;

import com.karolwrona.usermanagement.model.User;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface UserRepository extends JpaRepository<User, Long> {

    // Zapytanie do filtrowania użytkowników po nazwie roli
    @Query("SELECT u FROM User u JOIN u.roles r WHERE r.name = :roleName")
    List<User> findUsersByRoleName(@Param("roleName") String roleName);

    // Zapytanie do znajdowania użytkownika po nazwie użytkownika
    Optional<User> findByUsername(String username);
}
