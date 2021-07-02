package ar.lamansys.sgx.shared.security;

import ar.lamansys.sgx.shared.auth.user.SecurityContextUtils;

public class UserInfo {
	
	public static Integer getCurrentAuditor() {
		return SecurityContextUtils.getUserDetails().userId;
	}

}