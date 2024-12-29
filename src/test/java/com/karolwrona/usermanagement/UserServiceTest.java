package com.karolwrona.usermanagement;

import com.karolwrona.usermanagement.DTOs.UserDTO;
import com.karolwrona.usermanagement.model.Role;
import com.karolwrona.usermanagement.model.User;
import com.karolwrona.usermanagement.repository.RoleRepository;
import com.karolwrona.usermanagement.repository.UserRepository;
import com.karolwrona.usermanagement.service.UserService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;

import java.util.HashSet;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

class UserServiceTest {

    @Mock
    private UserRepository userRepository;

    @Mock
    private RoleRepository roleRepository;

    @InjectMocks
    private UserService userService;

    private User testUser;
    private Role testRole;

    @BeforeEach
    void setUp() {
        MockitoAnnotations.openMocks(this);

        testUser = new User();
        testUser.setId(1L);
        testUser.setUsername("TestUser");
        testUser.setPassword("password123");
        testUser.setRoles(new HashSet<>());

        testRole = new Role();
        testRole.setId(1L);
        testRole.setName("ROLE_USER");

        when(userRepository.findById(1L)).thenReturn(Optional.of(testUser));
        when(roleRepository.findById(1L)).thenReturn(Optional.of(testRole));
        when(userRepository.save(any(User.class))).thenAnswer(invocation -> invocation.getArgument(0));
    }

    @Test
    void shouldAssignRoleToUser() {
        // Testowana metoda
        UserDTO updatedUser = userService.assignRoleToUser(1L, 1L);

        // Asercje
        assertNotNull(updatedUser);
        assertTrue(updatedUser.getRoles().contains("ROLE_USER"), "User should have 'ROLE_USER' role assigned");
        verify(userRepository, times(1)).save(testUser);
    }

    @Test
    void shouldRemoveRoleFromUser() {
        // Dodanie roli do użytkownika
        testUser.addRole(testRole);

        // Testowana metoda
        UserDTO updatedUser = userService.removeRoleFromUser(1L, 1L);

        // Asercje
        assertNotNull(updatedUser);
        assertFalse(updatedUser.getRoles().contains("ROLE_USER"), "User should not have 'ROLE_USER' role anymore");
        verify(userRepository, times(1)).save(testUser);
    }
}