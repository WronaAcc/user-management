package com.karolwrona.usermanagement.service;

import com.karolwrona.usermanagement.model.Role;
import com.karolwrona.usermanagement.model.User;
import com.karolwrona.usermanagement.repository.RoleRepository;
import com.karolwrona.usermanagement.repository.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Optional;

@Service
public class UserService {

    @Autowired
    private UserRepository userRepository;

    @Autowired
    private RoleRepository roleRepository;

    /**
     * Pobierz wszystkich użytkowników.
     */
    public List<User> findAll() {
        return userRepository.findAll();
    }

    /**
     * Pobierz użytkowników z daną rolą.
     */
    public List<User> findUsersByRole(String roleName) {
        return userRepository.findUsersByRoleName(roleName);
    }

    /**
     * Zapisz nowego użytkownika. Sprawdza, czy użytkownik o podanym username istnieje.
     */
    public User save(User user) {
        Optional<User> existingUser = userRepository.findByUsername(user.getUsername());
        if (existingUser.isPresent()) {
            throw new RuntimeException("User with this username already exists");
        }
        return userRepository.save(user);
    }

    /**
     * Usuń użytkownika po ID.
     */
    public void deleteById(Long id) {
        userRepository.deleteById(id);
    }

    /**
     * Pobierz użytkownika po ID.
     */
    public Optional<User> findById(Long id) {
        return userRepository.findById(id);
    }

    /**
     * Przypisz rolę do użytkownika.
     */
    @Transactional
    public User assignRoleToUser(Long userId, Long roleId) {
        User user = userRepository.findById(userId)
                .orElseThrow(() -> new RuntimeException("User not found"));
        Role role = roleRepository.findById(roleId)
                .orElseThrow(() -> new RuntimeException("Role not found"));

        if (user.getRoles().contains(role)) {
            throw new RuntimeException("User already has this role assigned");
        }

        user.addRole(role);
        return userRepository.save(user);
    }

    /**
     * Usuń rolę od użytkownika.
     */
    @Transactional
    public User removeRoleFromUser(Long userId, Long roleId) {
        User user = userRepository.findById(userId)
                .orElseThrow(() -> new RuntimeException("User not found"));
        Role role = roleRepository.findById(roleId)
                .orElseThrow(() -> new RuntimeException("Role not found"));

        if (!user.getRoles().contains(role)) {
            throw new RuntimeException("User does not have this role");
        }

        user.removeRole(role);
        return userRepository.save(user);
    }

    public Optional<User> findByUsername(String username) {
        return userRepository.findByUsername(username);
    }

}