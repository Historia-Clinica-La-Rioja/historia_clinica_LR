package net.pladema.sgh.app.security.infraestructure.filters;

import ar.lamansys.sgx.shared.auth.user.SecurityContextUtils;
import ar.lamansys.sgx.shared.auth.user.SgxUserDetails;
import net.pladema.permissions.service.UserAssignmentService;
import net.pladema.sgh.app.security.infraestructure.authorization.InstitutionGrantedAuthority;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Component;
import org.springframework.web.filter.OncePerRequestFilter;

import javax.servlet.Filter;
import javax.servlet.FilterChain;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.util.Collection;
import java.util.Optional;
import java.util.stream.Collectors;

@Component
public class AuthorizationFilter extends OncePerRequestFilter implements Filter {
	
	private final Logger logger;

	private final UserAssignmentService userAssignmentService;

	public AuthorizationFilter(UserAssignmentService userAssignmentService) {
		this.logger = LoggerFactory.getLogger(this.getClass());
		this.userAssignmentService = userAssignmentService;
	}

	@Override
	protected void doFilterInternal(HttpServletRequest request, HttpServletResponse response, FilterChain chain)
			throws ServletException, IOException {

		Optional.ofNullable(SecurityContextUtils.getAuthentication())
				.map(auth -> new UsernamePasswordAuthenticationToken(
						auth.getPrincipal(),
						auth.getCredentials(),
						getAuthorities(((SgxUserDetails)auth.getPrincipal()).getUserId())))
				.ifPresent(opA -> SecurityContextHolder.getContext().setAuthentication(opA));

		logger.debug("Request {}", request);
		chain.doFilter(request, response);
		logger.debug("Response {}", response);
	}

	private Collection<GrantedAuthority> getAuthorities(Integer userId) {
		//Los permisos del usuario se obtienen de user_role
		return userAssignmentService.getRoleAssignment(userId)
				.stream()
				.map(InstitutionGrantedAuthority::new)
				.collect(Collectors.toList());
	}

}
