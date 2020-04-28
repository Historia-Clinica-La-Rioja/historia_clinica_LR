package net.pladema.user.service.impl;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.when;

import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.test.context.junit4.SpringRunner;

import net.pladema.user.repository.UserPasswordRepository;
import net.pladema.user.repository.entity.UserPassword;

@RunWith(SpringRunner.class)
public class UserPasswordServiceImplTest {
	private final static Integer USER_ID = 1008;

	@MockBean
	private UserPasswordRepository userPasswordRepository;

	@MockBean
	private BCryptPasswordEncoder bCryptPasswordEncoder;

	private UserPasswordServiceImpl userPasswordService;

	@Before
	public void setUp() {
		userPasswordService = new UserPasswordServiceImpl(userPasswordRepository, bCryptPasswordEncoder);
	}

	@Test
	public void createPassword_ok() {

		String encriptedPassword = "PasswordEncriptada";
		
		when(bCryptPasswordEncoder.encode(any())).thenReturn(encriptedPassword);
		UserPassword response = userPasswordService.createPassword(USER_ID, "Password");

		assertThat(response).isNotNull();

		assertThat(response.getPassword()).isNotNull().isEqualTo(encriptedPassword);

	}
}
