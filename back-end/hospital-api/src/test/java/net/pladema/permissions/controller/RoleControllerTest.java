package net.pladema.permissions.controller;

import net.pladema.BaseControllerTest;
import net.pladema.permissions.service.RoleService;
import net.pladema.user.service.UserService;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.MediaType;
import org.springframework.security.test.context.support.WithMockUser;
import org.springframework.test.context.junit4.SpringRunner;

import java.util.Locale;

import static net.pladema.permissions.RoleTestUtils.invalidIdLicense;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.log;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@RunWith(SpringRunner.class)
@WebMvcTest(RoleController.class)
public class RoleControllerTest extends BaseControllerTest {

	@Value("/roles")
	protected String userURL;
	
	@MockBean
	private UserService userService;
	
	@MockBean
	private RoleService roleService;
	
	@Before
	public void setup() {
	}

	@Test
	@WithMockUser(username="admin",roles={"ADMIN"})
	public void addRole_invalidId_ES() throws Exception {
		String body = objectMapper.writeValueAsString(invalidIdLicense());
		mockMvc.perform(post(userURL).content(body).contentType(MediaType.APPLICATION_JSON).locale(Locale.forLanguageTag("es-AR")))
			.andDo(log())
			.andExpect(status().isBadRequest())
			.andExpect(jsonPath("$.roleId").value("Id rol invalido"));
	}
	
	@Test
	@WithMockUser
	public void addRole_invalidId_EN() throws Exception {
		Locale locale = new Locale("en","US");
		String body = objectMapper.writeValueAsString(invalidIdLicense());
		mockMvc.perform(post(userURL).content(body).contentType(MediaType.APPLICATION_JSON).locale(locale))
			.andDo(log())
			.andExpect(status().isBadRequest())
			.andExpect(jsonPath("$.roleId").value("Role id invalid"));
	}

	
}
