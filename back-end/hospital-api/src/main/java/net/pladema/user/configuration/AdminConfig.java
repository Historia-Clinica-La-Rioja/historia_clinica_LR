package net.pladema.user.configuration;

import net.pladema.permissions.repository.enums.ERole;
import net.pladema.permissions.service.RoleService;
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

	private final RoleService roleService;

	public AdminConfig(UserService userService, UserPasswordService userPasswordService,
			RoleService roleService) {
		super();
		this.userService = userService;
		this.userPasswordService = userPasswordService;
		this.roleService = roleService;
	}

	@PostConstruct
	public void init() {
		Optional<User> admin = userService.getUser(adminMail);
		if (admin.isPresent())
			updateUser(admin.get());
		else createAdminUser();
	}

	private void createAdminUser() {
		User admin = new User();
		admin.setUsername(adminMail);
		admin.setEnable(true);
		admin = userService.addUser(admin);
		userPasswordService.addPassword(admin, adminPassword);
		roleService.createUserRole(admin.getId(), ERole.ADMIN);
		LOG.info("{}", "Admin created");
	}
	
	private void updateUser(User admin) {
		admin.setUsername(adminMail);
		admin.setEnable(true);
		admin = userService.updateUser(admin);
		userPasswordService.updatePassword(admin.getId(), adminPassword);
		roleService.updateAdminRole(admin.getId(), ERole.ADMIN);
		LOG.info("{}", "Admin updated");
	}
	
}
