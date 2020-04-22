package net.pladema.user.service.impl;

import static net.pladema.user.UserTestUtils.getUserRandomId;
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
import net.pladema.user.repository.entity.User;
import net.pladema.user.repository.entity.UserPassword;
import net.pladema.user.service.impl.UserPasswordServiceImpl;

@RunWith(SpringRunner.class)
public class UserPasswordServiceImplTest {

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
	public void testRandomPasswordGenerator() {
		User user = getUserRandomId("username@mail.com");
		String encriptedPassword = "PasswordEncriptada";
		
		when(bCryptPasswordEncoder.encode(any())).thenReturn(encriptedPassword);
		UserPassword response = userPasswordService.createPassword(user.getId(), "Password");

		assertThat(response).isNotNull();

		assertThat(response.getPassword()).isNotNull().isEqualTo(encriptedPassword);

	}
}
