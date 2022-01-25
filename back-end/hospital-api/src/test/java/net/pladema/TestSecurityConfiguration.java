package net.pladema;

import ar.lamansys.sgx.shared.auth.user.SgxUserDetails;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.core.authority.AuthorityUtils;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;

@Configuration
public class TestSecurityConfiguration {

	@Bean("basicUserDetailsService")
	public UserDetailsService basicUserDetailsService() {

		return new UserDetailsService() {
			@Override
			public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
				// Asume que el userId se puede extraer del final del username, separado por un -
				// ejemplo: chimuelo-13 (id=13), martita-98765 (id=98765)
				Integer userId = Integer.parseInt(username.split("-")[1]);
				return new SgxUserDetails(userId, username);
			}
		};
	}

	@Bean("UserDetailsServiceWithRole")
	public UserDetailsService userDetailsServiceWithRole() {

		return new UserDetailsService() {
			@Override
			public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
				// Asume que el userId se puede extraer del final del username, separado por un -
				// ejemplo: chimuelo-13 (id=13), martita-98765 (id=98765)
				Integer userId = Integer.parseInt(username.split("-")[1]);
				String roles = username.split("-")[2];
				return new SgxUserDetails(userId, username, AuthorityUtils.commaSeparatedStringToAuthorityList(roles));
			}
		};
	}
}
