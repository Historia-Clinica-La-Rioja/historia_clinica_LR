package net.pladema.sgh.app.security.infraestructure.input.rest;

import net.pladema.sgh.app.IntegrationTest;
import org.junit.jupiter.api.Disabled;
import org.junit.jupiter.api.Test;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.security.test.context.support.WithUserDetails;
import org.springframework.test.context.TestPropertySource;
import org.springframework.test.web.servlet.ResultActions;
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;

import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;


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
    @Disabled(value = "Falla en el ci, pero no local. Responde 200 cuando se espera un 401")
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