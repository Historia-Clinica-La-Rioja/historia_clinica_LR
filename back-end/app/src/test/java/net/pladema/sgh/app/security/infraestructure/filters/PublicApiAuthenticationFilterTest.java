package net.pladema.sgh.app.security.infraestructure.filters;

import ar.lamansys.sgx.auth.apiKey.infrastructure.input.service.ApiKeyExternalService;
import ar.lamansys.sgx.auth.apiKey.infrastructure.input.service.dto.ApiKeyInfoDto;
import net.pladema.permissions.repository.enums.ERole;
import net.pladema.permissions.service.UserAssignmentService;
import net.pladema.permissions.service.dto.RoleAssignment;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import javax.servlet.FilterChain;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.util.List;
import java.util.Optional;

import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class PublicApiAuthenticationFilterTest {

    private PublicApiAuthenticationFilter publicApiAuthenticationFilter;

    @Mock
    private ApiKeyExternalService apiKeyExternalService;

    @Mock
    private UserAssignmentService userAssignmentService;

    @Mock
    private HttpServletRequest request;

    @Mock
    private FilterChain chain;

    @Mock
    private HttpServletResponse response;

    @BeforeEach
    void setUp(){
        publicApiAuthenticationFilter = new PublicApiAuthenticationFilter("api-key", apiKeyExternalService, userAssignmentService);
    }

    @Test
    void testSuccess() throws IOException, ServletException {
        when(apiKeyExternalService.login(any())).thenReturn(Optional.of(new ApiKeyInfoDto(-14)));
        when(userAssignmentService.getRoleAssignment(-14)).thenReturn(List.of(new RoleAssignment(ERole.API_CONSUMER, -15)));
        when(request.getHeader("api-key")).thenReturn("api-key-value");

        FilterChain spyChain = spy(chain);
        spyChain.doFilter(request, response);
        verify(spyChain).doFilter(request, response);

        HttpServletResponse spyResponse = spy(response);
        spyResponse.setStatus(200);
        verify(spyResponse).setStatus(200);

        publicApiAuthenticationFilter.doFilterInternal(request, response, chain);
    }

    @Test
    void testFailLogin() throws IOException, ServletException {
        when(apiKeyExternalService.login(any())).thenReturn(Optional.empty());
        when(request.getHeader("api-key")).thenReturn("api-key-value");

        FilterChain spyChain = spy(chain);
        spyChain.doFilter(request, response);
        verify(spyChain).doFilter(request, response);

        HttpServletResponse spyResponse = spy(response);
        spyResponse.setStatus(200);
        verify(spyResponse).setStatus(200);

        publicApiAuthenticationFilter.doFilterInternal(request, response, chain);
    }
}