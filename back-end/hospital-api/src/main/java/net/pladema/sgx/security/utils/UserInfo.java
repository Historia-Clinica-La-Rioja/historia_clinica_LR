package net.pladema.sgx.security.utils;

import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;

public class UserInfo {

	private static boolean isAnonymousUser(Authentication authentication){
		return authentication.getPrincipal().equals("anonymousUser");
	}
	
	public static Integer getCurrentAuditor() {
		Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
		if (authentication == null || !authentication.isAuthenticated() || isAnonymousUser(authentication) )
			return -1;
		return (Integer) authentication.getPrincipal();
	}

}