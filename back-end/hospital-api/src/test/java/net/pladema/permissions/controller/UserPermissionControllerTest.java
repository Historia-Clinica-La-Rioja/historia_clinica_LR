package net.pladema.permissions.controller;

import net.pladema.BaseControllerTest;
import net.pladema.permissions.controller.UserPermissionController;
import net.pladema.permissions.service.LoggedUserService;
import net.pladema.permissions.service.dto.RoleAssignment;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.Mock;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.security.test.context.support.WithMockUser;
import org.springframework.test.context.junit4.SpringRunner;

import java.util.Arrays;
import java.util.List;
import java.util.Optional;

import static org.hamcrest.Matchers.hasSize;
import static org.junit.Assert.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@RunWith(SpringRunner.class)
@WebMvcTest(UserPermissionController.class)
public class UserPermissionControllerTest extends BaseControllerTest {

	@MockBean
	private LoggedUserService loggedUserService;

	@Test
	@WithMockUser
	public void getPermissions_admin() throws Exception {

		List<RoleAssignment> permissionAssignment = Arrays.asList(
				new RoleAssignment("ADMIN", null)
		);
		when(loggedUserService.getPermissionAssignment()).thenReturn(permissionAssignment);

		mockMvc.perform(get("/permissions"))
				.andDo(print())
				.andExpect(status().isOk())
				.andExpect(jsonPath("$.roleAssignments").isArray())
				.andExpect(jsonPath("$.roleAssignments", hasSize(1)))
				.andExpect(jsonPath("$.roleAssignments[0].role").value("ADMIN"));
	}

	@Test
	@WithMockUser
	public void getPermissions_hospital() throws Exception {

		List<RoleAssignment> permissionAssignment = Arrays.asList(
				new RoleAssignment("MEDICO", 99)
		);
		when(loggedUserService.getPermissionAssignment()).thenReturn(permissionAssignment);

		mockMvc.perform(get("/permissions"))
				.andDo(print())
				.andExpect(status().isOk())
				.andExpect(jsonPath("$.roleAssignments").isArray())
				.andExpect(jsonPath("$.roleAssignments", hasSize(1)))
				.andExpect(jsonPath("$.roleAssignments[0].role").value("MEDICO"))
				.andExpect(jsonPath("$.roleAssignments[0].institutionId").value(99));
	}

	@Test
	public void getAccount_noUser() throws Exception {
		mockMvc.perform(get("/permissions"))
				.andDo(print())
				.andExpect(status().isUnauthorized());
	}
}