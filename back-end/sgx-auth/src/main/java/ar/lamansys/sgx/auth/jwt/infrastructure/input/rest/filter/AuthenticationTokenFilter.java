package ar.lamansys.sgx.auth.jwt.infrastructure.input.rest.filter;

import java.util.Optional;

import ar.lamansys.sgx.auth.jwt.infrastructure.input.service.AuthenticationExternalService;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

@Component
public class AuthenticationTokenFilter extends JWTFilter {

	public AuthenticationTokenFilter(
			@Value("${token.header}") String tokenHeader,
			@Value("${token.secret}") String secret,
			AuthenticationExternalService authenticationExternalService
	) {
		super(
				secret,
				authenticationExternalService::getAppAuthentication,
				request -> Optional.ofNullable(request.getHeader(tokenHeader))
						.or(() -> Optional.ofNullable(request.getHeader("Authorization")))
		);
	}

}