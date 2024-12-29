package com.karolwrona.usermanagement;

import com.karolwrona.usermanagement.controller.AuthController;
import com.karolwrona.usermanagement.model.Role;
import com.karolwrona.usermanagement.service.RoleService;
import com.karolwrona.usermanagement.service.UserService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.springframework.http.MediaType;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;

import java.util.Optional;

import static org.mockito.Mockito.*;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.content;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

class AuthControllerTest {

    private MockMvc mockMvc;

    @Mock
    private RoleService roleService;

    @Mock
    private UserService userService;

    @Mock
    private PasswordEncoder passwordEncoder;

    @InjectMocks
    private AuthController authController;

    @BeforeEach
    void setUp() {
        MockitoAnnotations.openMocks(this);
        mockMvc = MockMvcBuilders.standaloneSetup(authController).build();

        // Przygotowanie zależności
        Role role = new Role();
        role.setId(1L);
        role.setName("ROLE_USER");

        when(roleService.findEntityByName("ROLE_USER")).thenReturn(Optional.of(role));
        when(passwordEncoder.encode(any(CharSequence.class))).thenReturn("encodedPassword");
        when(userService.save(any())).thenAnswer(invocation -> invocation.getArgument(0));
    }

    @Test
    void shouldRegisterUserSuccessfully() throws Exception {
        String newUserJson = """
                {
                  "username": "newuser",
                  "password": "newpassword"
                }
                """;

        mockMvc.perform(post("/api/public/register")
                        .contentType(MediaType.APPLICATION_JSON) // Określenie typu treści
                        .accept(MediaType.APPLICATION_JSON) // Oczekiwanie JSON w odpowiedzi
                        .content(newUserJson))
                .andExpect(status().isOk())
                .andExpect(content().string("User registered successfully"));
    }

    @Test
    void shouldReturnBadRequestForEmptyPassword() throws Exception {
        String invalidUserJson = """
                {
                  "username": "invaliduser",
                  "password": ""
                }
                """;

        mockMvc.perform(post("/api/public/register")
                        .contentType(MediaType.APPLICATION_JSON) // Określenie typu treści
                        .accept(MediaType.APPLICATION_JSON) // Oczekiwanie JSON w odpowiedzi
                        .content(invalidUserJson))
                .andExpect(status().isBadRequest());
    }
}