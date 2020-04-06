package net.pladema.user.service.impl;

import java.time.LocalDateTime;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Service;

import net.pladema.user.repository.UserPasswordRepository;
import net.pladema.user.repository.entity.User;
import net.pladema.user.repository.entity.UserPassword;
import net.pladema.user.service.UserPasswordService;

@Service
public class UserPasswordServiceImpl implements UserPasswordService {

	private static final Logger LOG = LoggerFactory.getLogger(UserPasswordServiceImpl.class);

	private final UserPasswordRepository userPasswordRepository;

	private final BCryptPasswordEncoder bCryptPasswordEncoder;

	public UserPasswordServiceImpl(UserPasswordRepository userPasswordRepository,
			BCryptPasswordEncoder bCryptPasswordEncoder) {
		super();
		this.userPasswordRepository = userPasswordRepository;
		this.bCryptPasswordEncoder = bCryptPasswordEncoder;
	}

	@Override
	public UserPassword addPassword(User user, String password) {
		UserPassword userPassword = createPassword(user, password);
		userPassword = userPasswordRepository.save(userPassword);
		LOG.debug("{}", "Password created");
		return userPassword;
	}

	protected UserPassword createPassword(User user, String password) {
		UserPassword userPassword = new UserPassword(user);
		userPassword.setPassword(bCryptPasswordEncoder.encode(password));
		userPassword.setHashAlgorithm("hashAlgorithm");
		userPassword.setSalt("salt");
		return userPassword;
	}

	@Override
	public boolean validCredentials(String password, Integer id) {
		return userPasswordRepository.getPasswordById(id)
				.map(pass -> bCryptPasswordEncoder.matches(password, pass)).orElse(false);
	}

	@Override
	public void updatePassword(Integer userId, String password) {
		userPasswordRepository.updatePassword(userId, bCryptPasswordEncoder.encode(password), LocalDateTime.now());
	}

	@Override
	public boolean passwordUpdated(Integer userId, LocalDateTime tokenDate) {
		return (userPasswordRepository.passwordUpdateAfter(userId, tokenDate) > 0);
	}

}
