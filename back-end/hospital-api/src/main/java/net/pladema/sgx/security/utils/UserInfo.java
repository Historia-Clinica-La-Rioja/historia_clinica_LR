package net.pladema.sgx.security.utils;

import net.pladema.permissions.repository.enums.ERole;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.context.SecurityContextHolder;

import java.util.Arrays;

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

	public static boolean hasProfessionalRole() {
		return SecurityContextHolder.getContext()
				.getAuthentication()
				.getAuthorities()
				.stream()
				.map(GrantedAuthority::getAuthority)
				.anyMatch(role -> Arrays.asList(ERole.PROFESIONAL_DE_SALUD.getValue(), ERole.ESPECIALISTA_MEDICO.getValue(), ERole.ENFERMERO.getValue()).contains(role));
	}
}