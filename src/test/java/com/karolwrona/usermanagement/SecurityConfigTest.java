package com.karolwrona.usermanagement;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.security.test.context.support.WithMockUser;
import org.springframework.test.web.servlet.MockMvc;

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@SpringBootTest
@AutoConfigureMockMvc
class SecurityConfigTest {

    @Autowired
    private MockMvc mockMvc;

    @Test
    void shouldAllowAccessToPublicEndpointsWithoutAuthentication() throws Exception {
        mockMvc.perform(get("/api/public/test"))
                .andExpect(status().isOk());
    }

    @Test
    @WithMockUser(roles = "USER")
    void shouldAllowAccessToAuthEndpointsWithAuthentication() throws Exception {
        mockMvc.perform(get("/api/auth/users"))
                .andExpect(status().isOk());
    }

    @Test
    void shouldReturnUnauthorizedForAuthEndpointsWithoutAuthentication() throws Exception {
        mockMvc.perform(get("/api/auth/users"))
                .andExpect(status().isUnauthorized());
    }
}
