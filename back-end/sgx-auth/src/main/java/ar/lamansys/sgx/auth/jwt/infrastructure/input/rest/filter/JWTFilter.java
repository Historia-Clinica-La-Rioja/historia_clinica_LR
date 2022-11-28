package ar.lamansys.sgx.auth.jwt.infrastructure.input.rest.filter;

import java.io.IOException;
import java.util.Optional;
import java.util.function.Function;

import javax.servlet.FilterChain;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.filter.OncePerRequestFilter;

import lombok.extern.slf4j.Slf4j;

@Slf4j
public abstract class JWTFilter extends OncePerRequestFilter implements AuthenticationFilter {
	protected final Function<String, Optional<Authentication>> authenticationLoader;
	protected final Function<HttpServletRequest, Optional<String>> tokenExtractor;

	protected JWTFilter(
			Function<String, Optional<Authentication>> authenticationLoader,
			Function<HttpServletRequest, Optional<String>> tokenExtractor
	) {
		this.authenticationLoader = authenticationLoader;
		this.tokenExtractor = tokenExtractor;
	}

	@Override
	protected void doFilterInternal(HttpServletRequest request, HttpServletResponse response, FilterChain chain)
			throws ServletException, IOException {

		tokenExtractor.apply(request)
				.map(this::removeBearer)
				.flatMap(authenticationLoader::apply)
				.ifPresent(opA -> SecurityContextHolder.getContext().setAuthentication(opA));

		log.debug("Request {}", request.getRequestURL());
		chain.doFilter(request, response);
		log.debug("Response {}", response.getStatus());
	}

	protected String removeBearer(String token) {
		return token.replaceFirst("^Bearer ", "");
	}


}
