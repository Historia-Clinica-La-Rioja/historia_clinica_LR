package net.pladema.sgx.session.infrastructure.input.rest;

import static org.hamcrest.Matchers.hasSize;
import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.log;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

import java.util.stream.Stream;

import ar.lamansys.sgx.shared.dates.configuration.LocalDateMapper;

import org.junit.jupiter.api.Test;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.security.test.context.support.WithMockUser;

import net.pladema.IntegrationController;
import net.pladema.authorization.infrastructure.input.rest.mapper.RoleNameMapper;
import net.pladema.permissions.repository.enums.ERole;
import net.pladema.permissions.service.dto.RoleAssignment;
import net.pladema.sgx.session.application.fetchloggeduserinfo.FetchLoggedUserInfo;
import net.pladema.sgx.session.application.fetchloggeduserroles.FetchLoggedUserRoles;

@WebMvcTest(LoggedUserController.class)
class LoggedUserControllerIntegrationTest extends IntegrationController {

	@MockBean
	private FetchLoggedUserRoles getLoggedUserRoles;

	@MockBean
	private FetchLoggedUserInfo getLoggedUserInfo;

	@MockBean
	private RoleNameMapper roleNameMapper;

	@MockBean
	private LocalDateMapper localDateMapper;

	@Test
	@WithMockUser
	void getPermissions_admin() throws Exception {
		Stream<RoleAssignment> permissionAssignment = Stream.of(
				new RoleAssignment(ERole.ADMINISTRADOR, null)
		);
		when(getLoggedUserRoles.execute()).thenReturn(permissionAssignment);
		when(roleNameMapper.getRoleDescription(ERole.ADMINISTRADOR)).thenReturn("Administrador");

		mockMvc.perform(get("/account/permissions")).andDo(print()).andExpect(status().isOk()).andExpect(jsonPath("$.roleAssignments").isArray()).andExpect(jsonPath("$.roleAssignments", hasSize(1))).andExpect(jsonPath("$.roleAssignments[0].role").value("ADMINISTRADOR"));
	}

	@Test
	@WithMockUser
	void getPermissions_hospital() throws Exception {

		Stream<RoleAssignment> permissionAssignment = Stream.of(
				new RoleAssignment(ERole.PROFESIONAL_DE_SALUD, 99)
		);
		when(getLoggedUserRoles.execute()).thenReturn(permissionAssignment);
		when(roleNameMapper.getRoleDescription(ERole.PROFESIONAL_DE_SALUD)).thenReturn("Personal de Salud");

		mockMvc.perform(get("/account/permissions")).andDo(print()).andExpect(status().isOk()).andExpect(jsonPath("$.roleAssignments").isArray()).andExpect(jsonPath("$.roleAssignments", hasSize(1))).andExpect(jsonPath("$.roleAssignments[0].role").value("PROFESIONAL_DE_SALUD")).andExpect(jsonPath("$.roleAssignments[0].institutionId").value(99));
	}

	@Test
	void getAccount_noUser() throws Exception {
		mockMvc.perform(get("/account/permissions")).andDo(print()).andExpect(status().isUnauthorized());
	}

	@Test
	void getInfo_noUser() throws Exception {
		mockMvc.perform(get("/info")).andDo(log()).andExpect(status().isUnauthorized());
	}

}