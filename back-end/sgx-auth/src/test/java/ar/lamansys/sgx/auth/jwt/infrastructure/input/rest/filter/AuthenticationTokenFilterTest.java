package ar.lamansys.sgx.auth.jwt.infrastructure.input.rest.filter;

import ar.lamansys.sgx.auth.jwt.infrastructure.input.service.AuthenticationExternalService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Disabled;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import javax.servlet.FilterChain;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;

import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
@Disabled
class AuthenticationTokenFilterTest {

	private final static String TOKEN_HEADER = "Authorization";

	@Mock
	private AuthenticationExternalService authenticationExternalService;

	@Mock
	private FilterChain chain;

	@Mock
	private HttpServletRequest request;

	@Mock
	private HttpServletResponse response;

	private AuthenticationTokenFilter authTokenFilter;

	@BeforeEach
	void setUp() {
		authTokenFilter = new AuthenticationTokenFilter(TOKEN_HEADER, "shhh", authenticationExternalService);
	}

	@Test
	void testAbsentAuthToken() throws IOException, ServletException {
		when(request.getHeader(TOKEN_HEADER)).thenReturn(null);

		FilterChain spyChain = spy(chain);
		spyChain.doFilter(request, response);
		verify(spyChain, times(1)).doFilter(request, response);

		HttpServletResponse spyResponse = spy(response);
		spyResponse.setStatus(401);
		verify(spyResponse, times(1)).setStatus(401);

		authTokenFilter.doFilter(request, response, chain);
	}
}
