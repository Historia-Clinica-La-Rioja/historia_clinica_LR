package ar.lamansys.sgx.shared.auth.user;

import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;

import java.util.Collection;

public class SgxUserDetails implements UserDetails {

	private static final long serialVersionUID = 1654234256805942674L;

	public final Integer userId;
	
	private String username;
	
	private String password;
	
	private Collection<? extends GrantedAuthority> authorities;
	
	private Boolean accountNonExpired = true;
	
	private Boolean accountNonLocked = true;
	
	private Boolean credentialsNonExpired = true;
	
	private Boolean enabled = true;

	public SgxUserDetails(Integer userId) {
		this.userId = userId;
	}

	public SgxUserDetails(Integer userId, String username) {
		this.userId = userId;
		this.username = username;
	}

	public SgxUserDetails(Integer userId, String username, Collection<? extends GrantedAuthority> authorities) {
		this.userId = userId;
		this.username = username;
		this.authorities = authorities;
	}

	@Override
	public Collection<? extends GrantedAuthority> getAuthorities() {
		return authorities;
	}

	@Override
	public String getPassword() {
		return password;
	}

	@Override
	public String getUsername() {
		return username;
	}

	public Integer getUserId() {
		return userId;
	}

	@Override
	public boolean isAccountNonExpired() {
		return accountNonExpired;
	}

	@Override
	public boolean isAccountNonLocked() {
		return accountNonLocked;
	}

	@Override
	public boolean isCredentialsNonExpired() {
		return credentialsNonExpired;
	}

	@Override
	public boolean isEnabled() {
		return enabled;
	}

}
