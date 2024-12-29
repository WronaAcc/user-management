package com.karolwrona.usermanagement;

import com.karolwrona.usermanagement.DTOs.RoleDTO;
import com.karolwrona.usermanagement.model.Role;
import com.karolwrona.usermanagement.repository.RoleRepository;
import com.karolwrona.usermanagement.service.RoleService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;

import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

class RoleServiceTest {

    @InjectMocks
    private RoleService roleService;

    @Mock
    private RoleRepository roleRepository;

    private Role testRole;
    private RoleDTO testRoleDTO;

    @BeforeEach
    void setup() {
        MockitoAnnotations.openMocks(this);

        // Tworzenie testowych danych
        testRole = new Role();
        testRole.setId(1L);
        testRole.setName("ROLE_ADMIN");

        testRoleDTO = new RoleDTO();
        testRoleDTO.setId(1L);
        testRoleDTO.setName("ROLE_ADMIN");
    }

    @Test
    void shouldSaveRole() {
        when(roleRepository.save(any(Role.class))).thenReturn(testRole);

        RoleDTO savedRole = roleService.save(testRoleDTO);

        assertNotNull(savedRole);
        assertEquals("ROLE_ADMIN", savedRole.getName());
        verify(roleRepository, times(1)).save(any(Role.class));
    }

    @Test
    void shouldThrowExceptionWhenSavingRoleWithExistingName() {
        when(roleRepository.findByName("ROLE_ADMIN")).thenReturn(Optional.of(testRole));

        Exception exception = assertThrows(IllegalArgumentException.class, () -> roleService.save(testRoleDTO));
        assertEquals("Role with this name already exists: ROLE_ADMIN", exception.getMessage());

        verify(roleRepository, times(0)).save(any(Role.class));
    }
}