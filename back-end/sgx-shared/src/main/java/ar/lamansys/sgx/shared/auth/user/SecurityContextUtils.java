package ar.lamansys.sgx.shared.auth.user;

import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;

public class SecurityContextUtils {

	private SecurityContextUtils() {}

	private static final SgxUserDetails ANONYMOUS_USER = new SgxUserDetails(-1, "anonymousUser");

	public static SgxUserDetails getUserDetails() {
		Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
		if (authentication == null || !authentication.isAuthenticated() ||
				authentication.getPrincipal().equals("anonymousUser"))
			return ANONYMOUS_USER;
		return new SgxUserDetails(((Integer) authentication.getPrincipal()));

	}

	public static Authentication getAuthentication() {
		return SecurityContextHolder.getContext().getAuthentication();
	}

}
