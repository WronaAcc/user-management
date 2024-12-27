package com.karolwrona.usermanagement;

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
import static org.mockito.Mockito.*;

class RoleServiceTest {

    @Mock
    private RoleRepository roleRepository;

    @InjectMocks
    private RoleService roleService;

    private Role testRole;

    @BeforeEach
    void setUp() {
        MockitoAnnotations.openMocks(this);
        testRole = new Role();
        testRole.setId(1L);
        testRole.setName("ROLE_ADMIN");
    }

    @Test
    void shouldSaveRole() {
        when(roleRepository.save(any(Role.class))).thenReturn(testRole);
        Role savedRole = roleService.save(testRole);

        assertNotNull(savedRole);
        assertEquals("ROLE_ADMIN", savedRole.getName());
        verify(roleRepository, times(1)).save(testRole);
    }

    @Test
    void shouldThrowExceptionWhenSavingRoleWithExistingName() {
        when(roleRepository.findByName("ROLE_ADMIN")).thenReturn(Optional.of(testRole));

        Exception exception = assertThrows(IllegalArgumentException.class, () -> roleService.save(testRole));
        assertEquals("Role with this name already exists: ROLE_ADMIN", exception.getMessage());
    }
}