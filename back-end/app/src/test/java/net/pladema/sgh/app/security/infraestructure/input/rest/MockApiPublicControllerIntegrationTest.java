package net.pladema.sgh.app.security.infraestructure.input.rest;

import net.pladema.sgh.app.IntegrationTest;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.security.test.context.support.WithUserDetails;
import org.springframework.test.context.TestPropertySource;
import org.springframework.test.web.servlet.ResultActions;
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;

import static org.junit.jupiter.api.Assertions.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;


@ExtendWith(MockitoExtension.class)
@SpringBootTest
@AutoConfigureMockMvc
@TestPropertySource("classpath:integration-test.properties")
class MockApiPublicControllerIntegrationTest extends IntegrationTest {

    @Test
    @WithUserDetails(value="user-24-API_CONSUMER,ROOT", userDetailsServiceBeanName="UserDetailsServiceWithRole")
    void success() throws Exception {
        getValidate()
                .andExpect(status().isOk());
    }

    @Test
    @WithUserDetails(value="user-24-ROOT", userDetailsServiceBeanName="UserDetailsServiceWithRole")
    void invalidRole() throws Exception {
        getValidate()
                .andExpect(status().isForbidden());
    }

    @Test
    void nonAuthenticated() throws Exception {
        getValidate()
                .andExpect(status().isUnauthorized());
    }

    private ResultActions getValidate() throws Exception {
        return mockMvc.perform(
                MockMvcRequestBuilders.get("/public-api/mock/info")
        );
    }
}