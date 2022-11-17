package ar.lamansys.sgx.auth.jwt.infrastructure.input.rest.filter;

import java.util.Arrays;
import java.util.Optional;
import java.util.function.Function;

import javax.servlet.http.Cookie;
import javax.servlet.http.HttpServletRequest;

import ar.lamansys.sgx.auth.jwt.domain.token.ETokenType;
import ar.lamansys.sgx.auth.jwt.infrastructure.input.service.AuthenticationExternalService;
import ar.lamansys.sgx.auth.jwt.infrastructure.output.token.TokenUtils;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.security.core.Authentication;
import org.springframework.stereotype.Component;

@Component
public class AuthenticationTokenFilter extends JWTFilter {

	public AuthenticationTokenFilter(
			@Value("${token.header}") String tokenHeader,
			@Value("${token.secret}") String secret,
			AuthenticationExternalService authenticationExternalService
	) {
		super(
				authenticationLoader(authenticationExternalService, secret),
				request -> readFromCookie(request)
						.or(() -> Optional.ofNullable(request.getHeader(tokenHeader)))
						.or(() -> Optional.ofNullable(request.getHeader("Authorization")))
		);
	}

	public static Function<String, Optional<Authentication>> authenticationLoader(
			AuthenticationExternalService authenticationExternalService,
			String secret
	) {
		return (String token) -> TokenUtils.parseToken(token, secret, ETokenType.NORMAL)
				.flatMap(tokenData -> authenticationExternalService.getAppAuthentication(tokenData.username));
	}

	public static Optional<String> readFromCookie(HttpServletRequest request) {
		Cookie[] cookies = request.getCookies();
		if (cookies == null) {
			return Optional.empty();
		}
		return Arrays.stream(cookies)
				.filter(c -> "token".equals(c.getName()))
				.map(Cookie::getValue)
				.findFirst();

	}

}