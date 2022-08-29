package ar.lamansys.sgx.auth.jwt.infrastructure.input.rest.filter;

import java.io.IOException;
import java.util.Optional;
import java.util.function.Function;

import javax.servlet.FilterChain;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.filter.OncePerRequestFilter;

import ar.lamansys.sgx.auth.jwt.domain.token.ETokenType;
import ar.lamansys.sgx.auth.jwt.infrastructure.output.token.TokenUtils;
import lombok.extern.slf4j.Slf4j;

@Slf4j
public abstract class JWTFilter extends OncePerRequestFilter implements AuthenticationFilter {

	private final String secret;
	private final Function<String, Optional<Authentication>> authenticationLoader;
	private final Function<HttpServletRequest, Optional<String>> tokenExtractor;

	protected JWTFilter(
			@Value("${token.secret}") String secret,
			Function<String, Optional<Authentication>> authenticationLoader,
			Function<HttpServletRequest, Optional<String>> tokenExtractor
	) {
		this.secret = secret;
		this.authenticationLoader = authenticationLoader;
		this.tokenExtractor = tokenExtractor;
	}

	@Override
	protected void doFilterInternal(HttpServletRequest request, HttpServletResponse response, FilterChain chain)
			throws ServletException, IOException {

		tokenExtractor.apply(request)
				.map(this::removeBearer)
				.flatMap(token -> TokenUtils.parseToken(token, secret, ETokenType.NORMAL))
				.flatMap(tokenData -> authenticationLoader.apply(tokenData.username))
				.ifPresent(opA -> SecurityContextHolder.getContext().setAuthentication(opA));

		log.debug("Request {}", request.getRequestURL());
		chain.doFilter(request, response);
		log.debug("Response {}", response.getStatus());
	}

	private String removeBearer(String token) {
		return token.replaceFirst("^Bearer ", "");
	}


}
