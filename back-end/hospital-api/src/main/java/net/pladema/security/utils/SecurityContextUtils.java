package net.pladema.security.utils;

import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;

import net.pladema.security.service.domain.SgxUserDetails;

public class SecurityContextUtils {

	private SecurityContextUtils() {}

	private static final SgxUserDetails ANONYMOUS_USER = new SgxUserDetails(-1);

	public static SgxUserDetails getUserDetails() {
		Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
		if (authentication == null || !authentication.isAuthenticated() ||
				authentication.getPrincipal().equals("anonymousUser"))
			return ANONYMOUS_USER;
		return new SgxUserDetails(((Integer) authentication.getPrincipal()));
	}

}
