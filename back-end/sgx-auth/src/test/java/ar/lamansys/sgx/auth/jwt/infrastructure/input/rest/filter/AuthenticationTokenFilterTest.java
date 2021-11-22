package ar.lamansys.sgx.auth.jwt.infrastructure.input.rest.filter;

import static org.mockito.Mockito.spy;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

import java.io.IOException;

import javax.servlet.FilterChain;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import ar.lamansys.sgx.auth.jwt.infrastructure.input.service.AuthenticationExternalService;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.test.context.junit.jupiter.SpringExtension;

@ExtendWith(SpringExtension.class)
class AuthenticationTokenFilterTest {

	private final static String TOKEN_HEADER = "Authorization";

	@MockBean
	private AuthenticationExternalService authenticationExternalService;

	@MockBean
	private FilterChain chain;

	@MockBean
	private HttpServletRequest request;

	@MockBean
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
		verify(spyChain).doFilter(request, response);

		HttpServletResponse spyResponse = spy(response);
		spyResponse.setStatus(401);
		verify(spyResponse).setStatus(401);

		authTokenFilter.doFilter(request, response, chain);
	}
}
