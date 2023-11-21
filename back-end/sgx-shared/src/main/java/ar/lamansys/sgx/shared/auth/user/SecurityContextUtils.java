package ar.lamansys.sgx.shared.auth.user;

import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;

import lombok.extern.slf4j.Slf4j;

@Slf4j
public class SecurityContextUtils {

	private SecurityContextUtils() {}

	public static SgxUserDetails getUserDetails() {
		Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
		if (authentication == null || !authentication.isAuthenticated() ||
				authentication.getPrincipal().equals("anonymousUser"))
			return SgxUserDetails.ANONYMOUS;
		return (SgxUserDetails)authentication.getPrincipal();

	}

	public static Authentication getAuthentication() {
		return SecurityContextHolder.getContext().getAuthentication();
	}

	public static void warn(String message) {
		var userDetails = getUserDetails();
		log.warn(
				// Envia un WARN al log con la información recolectada
				"Security warning. User id={} ({}): {}",
				userDetails.userId,
				userDetails.getUsername(),
				message
		);
	}

	public static void setAuthentication(Authentication authentication) {
		SecurityContextHolder.getContext().setAuthentication(authentication);
	}
}
