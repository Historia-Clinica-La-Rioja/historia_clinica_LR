package net.pladema.user.service.impl;

import ar.lamansys.sgx.shared.auditable.entity.Audit;
import net.pladema.user.repository.UserPasswordRepository;
import net.pladema.user.repository.entity.User;
import net.pladema.user.repository.entity.UserPassword;
import net.pladema.user.service.UserPasswordService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;

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
		UserPassword userPassword = createPassword(user.getId(), password);
		userPassword = userPasswordRepository.save(userPassword);
		LOG.debug("{}", "Password created");
		return userPassword;
	}

	protected UserPassword createPassword(Integer userId, String password) {
		UserPassword userPassword = new UserPassword();
		userPassword.setId(userId);
		userPassword.setPassword(bCryptPasswordEncoder.encode(password));
		userPassword.setHashAlgorithm("hashAlgorithm");
		userPassword.setSalt("salt");
		userPassword.setAudit(new Audit());
		return userPassword;
	}

	@Override
	public boolean validCredentials(String password, Integer id) {
		return userPasswordRepository.getPasswordById(id)
				.map(pass -> bCryptPasswordEncoder.matches(password, pass)).orElse(false);
	}

	@Override
	public void updatePassword(Integer userId, String password) {
		userPasswordRepository.save(createPassword(userId, password));
	}

	@Override
	public boolean passwordUpdated(Integer userId, LocalDateTime tokenDate) {
		return (userPasswordRepository.passwordUpdateAfter(userId, tokenDate) > 0);
	}

	@Override
	public void setPassword(Integer userId, String password) {
		UserPassword userPassword = createPassword(userId, password);
		userPasswordRepository.save(userPassword);
		LOG.debug("{}", "Password set");
	}

}
