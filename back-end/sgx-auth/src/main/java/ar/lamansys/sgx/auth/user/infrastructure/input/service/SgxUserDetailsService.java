package ar.lamansys.sgx.auth.user.infrastructure.input.service;

import java.util.Optional;

import ar.lamansys.sgx.auth.user.domain.user.model.UserBo;
import ar.lamansys.sgx.auth.user.domain.user.service.UserStorage;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

import ar.lamansys.sgx.shared.auth.user.SgxUserDetails;

@Service
public class SgxUserDetailsService implements UserDetailsService {

	private final UserStorage userStorage;

	public SgxUserDetailsService(UserStorage userInfoStorage) {
		this.userStorage = userInfoStorage;
	}

	@Override
	public UserDetails loadUserByUsername(String username) {
		return loadSgxUserByUsername(username)
				.orElseThrow(() -> new UsernameNotFoundException(String.format("User %s not found", username)));
	}

	public Optional<SgxUserDetails> loadSgxUserByUsername(String username) {
		return mapToUserDetails(userStorage.getUser(username));
	}

	private Optional<SgxUserDetails> mapToUserDetails(UserBo user) {
		if (user.isEnable())
			return Optional.of(toSgxUserDetails(user));
		return Optional.empty();
	}

	private SgxUserDetails toSgxUserDetails(UserBo user) {
		return new SgxUserDetails(user.getId(), user.getUsername());
	}
}