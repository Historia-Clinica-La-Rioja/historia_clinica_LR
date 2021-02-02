package net.pladema.sgx.security.utils;

import net.pladema.security.utils.SecurityContextUtils;

public class UserInfo {
	
	public static Integer getCurrentAuditor() {
		return SecurityContextUtils.getUserDetails().userId;
	}

}