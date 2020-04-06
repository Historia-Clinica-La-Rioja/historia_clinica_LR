package net.pladema.user.controller;

import net.pladema.BaseControllerTest;
import net.pladema.permissions.service.RoleService;
import net.pladema.user.controller.mappers.UserMapper;
import net.pladema.user.repository.entity.UserPassword;
import net.pladema.user.service.UserPasswordService;
import net.pladema.user.service.UserService;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.MediaType;
import org.springframework.security.test.context.support.WithMockUser;
import org.springframework.test.context.junit4.SpringRunner;

import java.util.Locale;

import static net.pladema.user.UserTestUtils.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.log;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@RunWith(SpringRunner.class)
@WebMvcTest(UserController.class)
public class UserControllerTest extends BaseControllerTest {

	private final static String userURL = "/users";
	
	@MockBean
	private UserService userService;
	
	@MockBean
	private RoleService licenceService;
	
	@MockBean
	private UserPasswordService userPasswordService;
	
	@MockBean
	private UserMapper userMapper;
	
	@Before
	public void setup() {
	}

	@Test
	@WithMockUser(authorities = {"ADMIN"})
	public void validUserDto() throws Exception {
		when(userService.addUser(any())).thenReturn(createUser("username@mail.com"));
		when(userMapper.mapNewUser(any())).thenReturn(createUser("username@mail.com"));
		when(userPasswordService.addPassword(any(), any())).thenReturn(new UserPassword());
		String body = objectMapper.writeValueAsString(addUserDtoValid());
		mockMvc.perform(post(userURL).content(body).contentType(MediaType.APPLICATION_JSON))
			.andDo(log())
			.andExpect(status().isCreated());
	}
	
	@Test
	@WithMockUser
	public void invalidEmailUserDto() throws Exception {
		Locale locale = new Locale("es","ES");
		String body = objectMapper.writeValueAsString(addUserDtoInvalidEmail());
		mockMvc.perform(post(userURL).content(body).contentType(MediaType.APPLICATION_JSON).locale(locale))
			.andDo(log())
			.andExpect(status().isBadRequest());
	}
	
	@Test
	@WithMockUser
	public void invalidUserDto_ES() throws Exception {
		Locale locale = new Locale("es","ES");
		String body = objectMapper.writeValueAsString(addUserDtoInvalid());
		mockMvc.perform(post(userURL).content(body).contentType(MediaType.APPLICATION_JSON).locale(locale))
			.andDo(log())
			.andExpect(status().isBadRequest())
			.andExpect(jsonPath("$.email").value("Correo obligatorio"));
	}

	@Test
	@WithMockUser
	public void invalidUserDto_EN() throws Exception {
		Locale locale = new Locale("en","US");
		String body = objectMapper.writeValueAsString(addUserDtoInvalid());
		mockMvc.perform(post(userURL).content(body).contentType(MediaType.APPLICATION_JSON).locale(locale))
			.andDo(log())
			.andExpect(status().isBadRequest())
			.andExpect(jsonPath("$.email").value("Email mandatory"));
	}

}
