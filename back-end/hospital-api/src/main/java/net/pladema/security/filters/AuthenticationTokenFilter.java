package net.pladema.security.filters;

import java.io.IOException;
import java.util.Optional;

import javax.servlet.FilterChain;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Component;
import org.springframework.web.filter.OncePerRequestFilter;

import net.pladema.security.service.SecurityService;

@Component
public class AuthenticationTokenFilter extends OncePerRequestFilter {

	private static final Logger LOG = LoggerFactory.getLogger(AuthenticationTokenFilter.class);

	@Value("${token.header}")
	private String tokenHeader;

	private final SecurityService securityService;

	public AuthenticationTokenFilter(SecurityService securityService) {
		super();
		this.securityService = securityService;
	}

	@Override
	protected void doFilterInternal(HttpServletRequest request, HttpServletResponse response, FilterChain chain)
			throws ServletException, IOException {

		Optional<String> authToken = Optional.ofNullable(request.getHeader(tokenHeader));
		authToken.ifPresent(token -> {
			if (securityService.validateCredentials(token))
				securityService.getAppAuthentication(token)
						.ifPresent(opA -> SecurityContextHolder.getContext().setAuthentication(opA));
		});
		LOG.debug("{}", request);
		LOG.debug("{}", response);
		chain.doFilter(request, response);
	}
}