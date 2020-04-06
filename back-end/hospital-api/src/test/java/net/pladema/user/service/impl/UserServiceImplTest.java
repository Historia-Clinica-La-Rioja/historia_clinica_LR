package net.pladema.user.service.impl;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.when;

import java.util.Optional;

import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.test.context.junit4.SpringRunner;

import net.pladema.user.repository.UserRepository;
import net.pladema.user.service.UserService;
import net.pladema.user.service.impl.UserServiceImpl;

@RunWith(SpringRunner.class)
public class UserServiceImplTest {

	@MockBean
	private UserRepository userRepository;
	
	private UserService userService;
	
	@Before
	public void setUp() {
		userService = new UserServiceImpl(userRepository);
	}
	
	@Test(expected = BadCredentialsException.class)	
	public void getUser_notExist() {
		when(userRepository.findByUsername(any())).thenReturn(Optional.empty());
		userService.getUser("123412334324");
	}
}
