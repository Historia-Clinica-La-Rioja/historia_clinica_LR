package net.pladema.user.service.impl;

import net.pladema.permissions.service.domain.UserBo;
import net.pladema.user.repository.UserRepository;
import net.pladema.user.repository.entity.User;
import net.pladema.user.service.UserService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.Optional;

@Service
public class UserServiceImpl implements UserService {

	private static final String INVALID_USERNAME = "invalid.username";

	private static final Logger LOG = LoggerFactory.getLogger(UserServiceImpl.class);

	private final UserRepository userRepository;

	public UserServiceImpl(UserRepository userRepository) {
		super();
		this.userRepository = userRepository;
	}

	public User addUser(User user) {
		user = userRepository.save(user);
		LOG.debug("New user -> {}", user);
		return user;
	}

	@Override
	public void setEnable(User user, Boolean status) {
		userRepository.changeStatusAccount(user.getId(), status);
	}

	@Override
	public Optional<User> getUser(String username) {
		return userRepository.findByUsername(username);
	}

	@Override
	public Integer getUserId(String username) {
		return userRepository.getUserId(username).orElseThrow(() -> new BadCredentialsException(INVALID_USERNAME));
	}

	@Override
	public void updateLoginDate(Integer id) {
		userRepository.updateLoginDate(id, LocalDateTime.now());
	}

	@Override
	public boolean isEnable(String username) {
		return userRepository.isEnable(username);
	}

	@Override
	public Optional<UserBo> getUser(Integer userId) {
		Optional<User> userOpt = userRepository.findById(userId);
		return userOpt.map(UserBo::new);
	}
}
