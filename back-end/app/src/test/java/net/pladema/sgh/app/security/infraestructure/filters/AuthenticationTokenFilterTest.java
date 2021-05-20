package net.pladema.sgh.app.security.infraestructure.filters;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.spy;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

import java.io.IOException;

import javax.servlet.FilterChain;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.test.context.junit4.SpringRunner;

import io.jsonwebtoken.ExpiredJwtException;
import net.pladema.security.service.SecurityService;

@RunWith(SpringRunner.class)
public class AuthenticationTokenFilterTest {

	@Value("${token.header}")
	private String tokenHeader;

	@MockBean
	private SecurityService securityService;

	@MockBean
	private FilterChain chain;

	@MockBean
	private HttpServletRequest request;

	@MockBean
	private HttpServletResponse response;

	private AuthenticationTokenFilter authTokenFilter;

	@Before
	public void setUp() {
		authTokenFilter = new AuthenticationTokenFilter(securityService);
	}

	@Test
	public void testAbsentAuthToken() throws IOException, ServletException {
		when(request.getHeader(tokenHeader)).thenReturn(null);
		when(securityService.validateCredentials(any())).thenThrow(new ExpiredJwtException(null, null, tokenHeader));

		FilterChain spyChain = spy(chain);
		spyChain.doFilter(request, response);
		verify(spyChain).doFilter(request, response);

		HttpServletResponse spyResponse = spy(response);
		spyResponse.setStatus(401);
		verify(spyResponse).setStatus(401);

		authTokenFilter.doFilter(request, response, chain);
	}
}
