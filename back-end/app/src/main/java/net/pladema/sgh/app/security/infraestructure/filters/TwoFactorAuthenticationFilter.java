package net.pladema.sgh.app.security.infraestructure.filters;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import java.util.function.Function;

import javax.servlet.http.HttpServletRequest;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.web.util.matcher.AntPathRequestMatcher;
import org.springframework.stereotype.Component;

import ar.lamansys.sgx.auth.jwt.domain.token.ETokenType;
import ar.lamansys.sgx.auth.jwt.infrastructure.input.rest.filter.AuthenticationTokenFilter;
import ar.lamansys.sgx.auth.jwt.infrastructure.input.rest.filter.JWTFilter;
import ar.lamansys.sgx.auth.jwt.infrastructure.input.service.AuthenticationExternalService;
import ar.lamansys.sgx.auth.jwt.infrastructure.output.token.TokenUtils;
import lombok.extern.slf4j.Slf4j;
import net.pladema.permissions.repository.enums.ERole;
import net.pladema.permissions.service.dto.RoleAssignment;
import net.pladema.sgh.app.security.infraestructure.authorization.InstitutionGrantedAuthority;


@Slf4j
@Component
public class TwoFactorAuthenticationFilter extends JWTFilter {

	protected TwoFactorAuthenticationFilter(
			@Value("${token.secret}") String secret,
			@Value("${token.header}") String tokenHeader,
			AuthenticationExternalService authenticationExternalService) {
		super(
				authenticationLoader(authenticationExternalService, secret),
				request -> AuthenticationTokenFilter.readFromCookie(request)
						.or(() -> Optional.ofNullable(request.getHeader(tokenHeader)))
						.or(() -> Optional.ofNullable(request.getHeader("Authorization")))
		);
	}

	public static Function<String, Optional<Authentication>> authenticationLoader(
			AuthenticationExternalService authenticationExternalService,
			String secret
	) {
		return (String token) -> TokenUtils.parseToken(token, secret, ETokenType.PARTIALLY_AUTHENTICATED)
				.flatMap(tokenData -> authenticationExternalService.getAppAuthentication(tokenData.username))
				.map(TwoFactorAuthenticationFilter::loadPermission);
	}

	private static Authentication loadPermission(Authentication authentication) {
		List<GrantedAuthority> authorities = new ArrayList<>();
		authorities.add(new InstitutionGrantedAuthority(new RoleAssignment(ERole.PARTIALLY_AUTHENTICATED, null)));
		return new UsernamePasswordAuthenticationToken(
				authentication.getPrincipal(),
				authentication.getCredentials(),
				authorities
		);
	}

	@Override
	public boolean shouldNotFilter(HttpServletRequest request) {
		boolean matchesPermission = new AntPathRequestMatcher("/account/permissions").matches(request);
		boolean matchesTwoFactorAuthentication = new AntPathRequestMatcher("/auth/login-2fa").matches(request);
		boolean result = !(matchesPermission || matchesTwoFactorAuthentication);
		log.debug("Should not filter request {} ?, result {}", request.getPathInfo(), result);
		return result;
	}

}
