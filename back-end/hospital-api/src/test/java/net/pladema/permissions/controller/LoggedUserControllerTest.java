package net.pladema.permissions.controller;

import net.pladema.UnitController;
import net.pladema.permissions.controller.mappers.UserInfoMapper;
import net.pladema.permissions.repository.enums.ERole;
import net.pladema.permissions.service.LoggedUserService;
import net.pladema.permissions.service.dto.RoleAssignment;
import net.pladema.person.controller.service.PersonExternalService;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.security.test.context.support.WithMockUser;
import org.springframework.test.context.junit4.SpringRunner;

import java.util.Arrays;
import java.util.List;

import static org.hamcrest.Matchers.hasSize;
import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.log;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@RunWith(SpringRunner.class)
@WebMvcTest(LoggedUserController.class)
public class LoggedUserControllerTest extends UnitController {

	@MockBean
	private LoggedUserService loggedUserService;

	@MockBean
	private PersonExternalService personExternalService;

	@MockBean
	private UserInfoMapper userInfoMapper;

	@Test
	@WithMockUser
	public void getPermissions_admin() throws Exception {

		List<RoleAssignment> permissionAssignment = Arrays.asList(
				new RoleAssignment(ERole.ADMINISTRADOR, null)
		);
		when(loggedUserService.getPermissionAssignment()).thenReturn(permissionAssignment);

		mockMvc.perform(get("/account/permissions"))
				.andDo(log())
				.andExpect(status().isOk())
				.andExpect(jsonPath("$.roleAssignments").isArray())
				.andExpect(jsonPath("$.roleAssignments", hasSize(1)))
				.andExpect(jsonPath("$.roleAssignments[0].role").value("ADMINISTRADOR"));
	}

	@Test
	@WithMockUser
	public void getPermissions_hospital() throws Exception {

		List<RoleAssignment> permissionAssignment = Arrays.asList(
				new RoleAssignment(ERole.PROFESIONAL_DE_SALUD, 99)
		);
		when(loggedUserService.getPermissionAssignment()).thenReturn(permissionAssignment);

		mockMvc.perform(get("/account/permissions"))
				.andDo(log())
				.andExpect(status().isOk())
				.andExpect(jsonPath("$.roleAssignments").isArray())
				.andExpect(jsonPath("$.roleAssignments", hasSize(1)))
				.andExpect(jsonPath("$.roleAssignments[0].role").value("PROFESIONAL_DE_SALUD"))
				.andExpect(jsonPath("$.roleAssignments[0].institutionId").value(99));
	}

	@Test
	public void getAccount_noUser() throws Exception {
		mockMvc.perform(get("/account/permissions"))
				.andDo(log())
				.andExpect(status().isUnauthorized());
	}
}