package net.pladema.sgh.app.security.infraestructure.filters;

import ar.lamansys.sgx.shared.auth.user.SecurityContextUtils;
import ar.lamansys.sgx.shared.auth.user.SgxUserDetails;
import ca.uhn.fhir.rest.server.exceptions.AuthenticationException;
import lombok.RequiredArgsConstructor;

import lombok.extern.slf4j.Slf4j;

import net.pladema.hl7.supporting.security.ServerAuthInterceptor;

import net.pladema.permissions.repository.enums.ERole;
import net.pladema.permissions.service.RoleAssignmentAuthority;

import net.pladema.permissions.service.dto.RoleAssignment;

import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.web.util.matcher.AntPathRequestMatcher;
import org.springframework.stereotype.Component;
import org.springframework.web.filter.OncePerRequestFilter;

import javax.servlet.FilterChain;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import java.io.IOException;
import java.util.List;
import java.util.Optional;

/**
 * This filter applies on requests to the fhir api (api/fhir/*).
 * The logic is a follows:
 * If the Authorization header is present (Authorization: Bearer token-value) and federar is enabled
 * (see InteroperabilityCondition) the token is sent to federar for validation. If this is successful
 * the logged user gains the FHIR_ACCESS_ALL_RESOURCES role. The user is unknown so we use -1 as the userId.
 * Otherwise, we call the PublicApiAuthenticationFilter. On its own, the public api filter only applies to /public-api/* urls.
 * Calling it from this filter does not make it apply 2 times.
 */
@Slf4j
@RequiredArgsConstructor
@Component
public class FHIRApiAuthenticationFilter extends OncePerRequestFilter {

	private final Optional<ServerAuthInterceptor> serverAuthInterceptor;
	private final PublicApiAuthenticationFilter publicApiAuthenticationFilter;

	@Override
	public boolean shouldNotFilter(HttpServletRequest request) {
		var matcher = new AntPathRequestMatcher("/fhir/**");
		boolean result = !matcher.matches(request);
		log.debug("Should not filter request {} ?, result {}", request.getPathInfo(), result);
		return result;
	}

	@Override
	protected void doFilterInternal(HttpServletRequest httpServletRequest, HttpServletResponse httpServletResponse, FilterChain filterChain) throws ServletException, IOException {
		String accessToken = httpServletRequest.getHeader(ServerAuthInterceptor.HEADER);
		/**
		 * The client sent the Authorization header and federar is enabled
		 */
		if (accessToken != null && serverAuthInterceptor.isPresent()) {
			var interceptor = serverAuthInterceptor.get();
			try {
				interceptor.validToken(accessToken);
				var accessAllFhirAuthority = new RoleAssignmentAuthority(new RoleAssignment(ERole.FHIR_ACCESS_ALL_RESOURCES, -1));
				var auth = new UsernamePasswordAuthenticationToken(new SgxUserDetails(-1), "", List.of(accessAllFhirAuthority));
				SecurityContextUtils.setAuthentication(auth);
			} catch (AuthenticationException e) {
				log.debug("Invalid federar token");
			}
			log.debug("Request {}", httpServletRequest);
			filterChain.doFilter(httpServletRequest, httpServletResponse);
			log.debug("Response {}", httpServletResponse);
		}
		/**
		 * Try to apply the public api filter. Since we are on a /fhir/** url it wouldn't be applied otherwise.
		 */
		else {
			publicApiAuthenticationFilter.doFilterInternal(httpServletRequest, httpServletResponse, filterChain);
		}
	}

}

