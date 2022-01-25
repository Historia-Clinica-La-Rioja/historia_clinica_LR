package net.pladema.sgh.app.security.infraestructure.input.rest;

import net.pladema.permissions.repository.enums.ERole;
import net.pladema.permissions.service.UserAssignmentService;
import net.pladema.permissions.service.dto.RoleAssignment;
import net.pladema.sgh.app.IntegrationTest;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Disabled;
import org.junit.jupiter.api.Test;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.security.test.context.support.WithUserDetails;
import org.springframework.test.context.TestPropertySource;
import org.springframework.test.web.servlet.ResultActions;
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;

import java.util.List;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;


@SpringBootTest
@AutoConfigureMockMvc
@TestPropertySource("classpath:integration-test.properties")
@Disabled
class MockApiPublicControllerIntegrationTest extends IntegrationTest {

    @MockBean
    private UserAssignmentService userAssignmentService;

    @BeforeEach
    void setUp(){
        this.buildMockMvc();
    }

    @Test
    @WithUserDetails(value="user-24-API_CONSUMER", userDetailsServiceBeanName="UserDetailsServiceWithRole")
    void success() throws Exception {
        when(userAssignmentService.getRoleAssignment(any()))
                .thenReturn(List.of(new RoleAssignment(ERole.API_CONSUMER, -1)));
        getValidate()
                .andExpect(status().isOk());
    }

    @Test
    @WithUserDetails(value="user-24-ROOT2", userDetailsServiceBeanName="UserDetailsServiceWithRole")
    @Disabled
    void invalidRole() throws Exception {
        when(userAssignmentService.getRoleAssignment(any()))
                .thenReturn(List.of(new RoleAssignment(ERole.ROOT, -1)));
        getValidate()
                .andExpect(status().isForbidden());
    }

    @Test
    @Disabled
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