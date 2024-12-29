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
import org.springframework.security.crypto.password.PasswordEncoder;

import java.util.HashSet;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

class UserServiceTest {

    @Mock
    private UserRepository userRepository;

    @Mock
    private RoleRepository roleRepository;

    @Mock
    private PasswordEncoder passwordEncoder;

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

        when(passwordEncoder.encode(any(CharSequence.class))).thenAnswer(invocation -> "encoded_" + invocation.getArgument(0));
    }

    @Test
    void shouldAssignRoleToUser() {
        UserDTO updatedUser = userService.assignRoleToUser(1L, 1L);

        assertNotNull(updatedUser);
        assertTrue(updatedUser.getRoles().contains("ROLE_USER"), "User should have 'ROLE_USER' role assigned");
        verify(userRepository, times(1)).save(testUser);
    }

    @Test
    void shouldRemoveRoleFromUser() {
        testUser.addRole(testRole);

        UserDTO updatedUser = userService.removeRoleFromUser(1L, 1L);

        assertNotNull(updatedUser);
        assertFalse(updatedUser.getRoles().contains("ROLE_USER"), "User should not have 'ROLE_USER' role anymore");
        verify(userRepository, times(1)).save(testUser);
    }

    @Test
    void shouldNotSaveUserWithWeakPassword() {
        UserDTO userDTO = new UserDTO();
        userDTO.setUsername("WeakUser");
        userDTO.setEmail("weak@example.com");
        userDTO.setPassword("weak");

        IllegalArgumentException exception = assertThrows(IllegalArgumentException.class, () -> {
            userService.save(userDTO);
        });

        assertEquals("Password does not meet strength requirements.", exception.getMessage());
    }

    @Test
    void shouldNotSaveUserWithExistingUsername() {
        UserDTO userDTO = new UserDTO();
        userDTO.setUsername("TestUser");
        userDTO.setEmail("new@example.com");
        userDTO.setPassword("Strong@123");

        when(userRepository.findByUsername("TestUser"))
                .thenReturn(Optional.of(testUser));

        IllegalArgumentException exception = assertThrows(IllegalArgumentException.class, () -> {
            userService.save(userDTO);
        });

        assertEquals("User with this username already exists: TestUser", exception.getMessage());
    }

    @Test
    void shouldSaveUserWithStrongPasswordAndUniqueData() {
        UserDTO userDTO = new UserDTO();
        userDTO.setUsername("NewUser");
        userDTO.setEmail("new@example.com");
        userDTO.setPassword("Strong@123");

        when(userRepository.findByUsername("NewUser")).thenReturn(Optional.empty());
        when(userRepository.findByEmail("new@example.com")).thenReturn(Optional.empty());

        // Act
        UserDTO savedUser = userService.save(userDTO);

        // Assert
        assertNotNull(savedUser);
        assertEquals(userDTO.getUsername(), savedUser.getUsername());
        assertEquals(userDTO.getEmail(), savedUser.getEmail());
        verify(userRepository, times(1)).save(any(User.class));

        verify(passwordEncoder, times(1)).encode("Strong@123");
    }
}