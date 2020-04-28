package net.pladema.user.configuration;

import net.pladema.permissions.repository.enums.ERole;
import net.pladema.permissions.service.RoleService;
import net.pladema.permissions.service.UserAssignmentService;
import net.pladema.user.repository.entity.User;
import net.pladema.user.service.UserPasswordService;
import net.pladema.user.service.UserService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Profile;

import javax.annotation.PostConstruct;
import java.util.Optional;

@Configuration
@Profile("!test")
public class AdminConfig {

	private static final Logger LOG = LoggerFactory.getLogger(AdminConfig.class);
	
	private static final String ADMIN = "Admin";

	@Value("${admin.mail}")
	private String adminMail;

	@Value("${admin.password}")
	private String adminPassword;

	private final UserService userService;

	private final UserPasswordService userPasswordService;

	private final UserAssignmentService userAssignmentService;

	private final RoleService roleService;

	public AdminConfig(UserService userService, UserPasswordService userPasswordService, UserAssignmentService userAssignmentService, RoleService roleService) {
		super();
		this.userService = userService;
		this.userPasswordService = userPasswordService;
		this.userAssignmentService = userAssignmentService;
		this.roleService = roleService;
	}

	@PostConstruct
	public void init() {
		roleService.updateRolesStore();

		User admin = userService.getUser(adminMail)
				.orElseGet(() -> createAdminUser());
		updateUser(admin);
	}

	private User createAdminUser() {
		User admin = new User();
		admin.setUsername(adminMail);
		User saved = userService.addUser(admin);
		LOG.info("Admin created with id {}", saved.getId());
		return saved;
	}
	
	private void updateUser(User admin) {
		userService.setEnable(admin, true);
		userPasswordService.updatePassword(admin.getId(), adminPassword);
		userAssignmentService.saveUserRole(admin.getId(), ERole.ADMIN, null);
		LOG.info("{}", "Admin updated");
	}
	
}
