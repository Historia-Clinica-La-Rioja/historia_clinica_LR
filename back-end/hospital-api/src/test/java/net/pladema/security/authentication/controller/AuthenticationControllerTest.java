package net.pladema.security.authentication.controller;

import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

import ar.lamansys.sgx.shared.recaptcha.service.ICaptchaService;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.ArgumentMatchers;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.MediaType;
import org.springframework.security.test.context.support.WithMockUser;
import org.springframework.test.context.junit4.SpringRunner;

import net.pladema.UnitController;
import net.pladema.security.authentication.controller.mapper.JWTokenMapper;
import net.pladema.security.authentication.controller.mapper.LoginMapper;
import net.pladema.security.authentication.service.AuthenticationService;
import net.pladema.user.service.UserService;

@RunWith(SpringRunner.class)
@WebMvcTest(AuthenticationController.class)
public class AuthenticationControllerTest extends UnitController {

	@Value("/auth")
	protected String authURL;

	@MockBean
	private AuthenticationService authenticationService;
	
	@MockBean
	private LoginMapper loginMapper;
	
	@MockBean
	private JWTokenMapper jWTokenMapper;
	
	@MockBean
	private UserService userService;

	@MockBean
	private ICaptchaService iCaptchaService;

	@Before
	public void setup() {
	}
	
	@Test
	@WithMockUser
	public void testCors() throws Exception {
		
		when(userService.isEnable(ArgumentMatchers.anyString())).thenReturn(true);
		String body = "{\"username\":\"pepe@pepe.com\",\"password\":\"password\"}";
		mockMvc.perform(post(authURL).content(body).contentType(MediaType.APPLICATION_JSON)
				.header("Access-Control-Request-Method", "GET")
				.header("Origin", "http://www.someurl.com")
				.header("recaptcha", "somestring"))
			.andExpect(status().isOk());
	}
	

	

}
