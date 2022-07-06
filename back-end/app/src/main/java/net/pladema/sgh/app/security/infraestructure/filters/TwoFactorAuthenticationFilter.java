package net.pladema.sgh.app.security.infraestructure.filters;

import ar.lamansys.sgx.auth.jwt.domain.token.ETokenType;
import ar.lamansys.sgx.auth.jwt.infrastructure.input.rest.filter.JWTFilter;
import ar.lamansys.sgx.auth.jwt.infrastructure.input.service.AuthenticationExternalService;
import ar.lamansys.sgx.auth.jwt.infrastructure.output.token.TokenUtils;
import lombok.extern.slf4j.Slf4j;

import net.pladema.permissions.repository.enums.ERole;
import net.pladema.permissions.service.dto.RoleAssignment;
import net.pladema.sgh.app.security.infraestructure.authorization.InstitutionGrantedAuthority;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.web.util.matcher.AntPathRequestMatcher;
import org.springframework.stereotype.Component;

import javax.servlet.FilterChain;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;


@Slf4j
@Component
public class TwoFactorAuthenticationFilter extends JWTFilter {

	protected TwoFactorAuthenticationFilter(
			@Value("${token.secret}") String secret,
			@Value("${token.header}") String tokenHeader,
			AuthenticationExternalService authenticationExternalService) {
		super(
				secret,
				authenticationExternalService::getAppAuthentication,
				request -> Optional.ofNullable(request.getHeader(tokenHeader))
				.or(() -> Optional.ofNullable(request.getHeader("Authorization")))
		);
	}

	@Override
	protected void doFilterInternal(HttpServletRequest request, HttpServletResponse response, FilterChain filterChain)
			throws ServletException, IOException {
		tokenExtractor.apply(request)
				.map(this::removeBearer)
				.flatMap(token -> TokenUtils.parseToken(token, secret, ETokenType.PARTIALLY_AUTHENTICATED))
				.flatMap(tokenData -> authenticationLoader.apply(tokenData.username))
				.ifPresent(this::loadPermission);

		log.debug("Request {}", request.getRequestURL());
		filterChain.doFilter(request, response);
		log.debug("Response {}", response.getStatus());
	}

	private void loadPermission(Authentication authentication) {
		List<GrantedAuthority> authorities = new ArrayList<>();
		authorities.add(new InstitutionGrantedAuthority(new RoleAssignment(ERole.PARTIALLY_AUTHENTICATED, null)));
		Optional.ofNullable(authentication)
				.map(auth -> new UsernamePasswordAuthenticationToken(
						auth.getPrincipal(),
						auth.getCredentials(),
						authorities))
				.ifPresent(op -> SecurityContextHolder.getContext().setAuthentication(op));
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
