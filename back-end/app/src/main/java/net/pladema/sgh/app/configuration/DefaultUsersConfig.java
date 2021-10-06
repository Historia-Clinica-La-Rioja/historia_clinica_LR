package net.pladema.sgh.app.configuration;

import ar.lamansys.sgx.auth.user.infrastructure.input.service.UserExternalService;
import ar.lamansys.sgx.auth.user.infrastructure.input.service.dto.UserInfoDto;
import net.pladema.permissions.repository.entity.UserRolePK;
import net.pladema.permissions.repository.enums.ERole;
import net.pladema.permissions.service.RoleService;
import net.pladema.permissions.service.UserAssignmentService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Profile;

import javax.annotation.PostConstruct;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Configuration
@Profile("!test")
public class DefaultUsersConfig {

	private static final Logger LOG = LoggerFactory.getLogger(DefaultUsersConfig.class);

	private Map<String, DefaultUserInfoBo> defaultUsers = new HashMap<>();

	@Value("${admin.password}")
	private String adminPassword;

	private final UserExternalService userExternalService;

	private final UserAssignmentService userAssignmentService;

	private final RoleService roleService;

	public DefaultUsersConfig(UserExternalService userExternalService,
							  UserAssignmentService userAssignmentService,
							  RoleService roleService) {
		super();
		this.userExternalService = userExternalService;
		this.userAssignmentService = userAssignmentService;
		this.roleService = roleService;
	}

	@PostConstruct
	public void init() {
		defaultUsers.put("admin@example.com", new DefaultUserInfoBo(adminPassword,
				List.of(new DefaultUserRolBo(ERole.ROOT, UserRolePK.UNDEFINED_ID.intValue()))));
		roleService.updateRolesStore();

		defaultUsers.forEach((key, value) -> {
			UserInfoDto user = userExternalService.getUser(key)
					.orElseGet(() -> createUser(key, value.getPassword()));
			updateUser(user, value);
		});
	}

	private UserInfoDto createUser(String username, String password) {
		userExternalService.registerUser(username, null, password);
		return userExternalService.getUser(username).get();
	}
	
	private void updateUser(UserInfoDto user, DefaultUserInfoBo defaultUserInfoBo) {
		userExternalService.enableUser(user.getUsername());
		userExternalService.updatePassword(user.getUsername(), defaultUserInfoBo.getPassword());
		defaultUserInfoBo.getRoles().forEach(eRole ->
			userAssignmentService.saveUserRole(user.getId(), eRole.getRol(), eRole.getInstitutionId())
		);
		LOG.info("User updated {}", user.getUsername());
	}

}
