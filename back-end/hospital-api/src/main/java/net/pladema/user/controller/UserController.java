package net.pladema.user.controller;

import io.swagger.annotations.Api;
import net.pladema.permissions.service.RoleService;
import net.pladema.user.controller.mappers.UserMapper;
import net.pladema.user.repository.projections.PageableUsers;
import net.pladema.user.service.UserPasswordService;
import net.pladema.user.service.UserService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.ApplicationEventPublisher;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.data.web.PageableDefault;
import org.springframework.data.web.SortDefault;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/users")
@Api(value = "User", tags = { "User" })
public class UserController {

	private static final String USER_VALID = "User valid";

	private static final Logger LOG = LoggerFactory.getLogger(UserController.class);

	@Value("${frontend.loginpage}")
	protected String loginPage;

	private final UserService userService;

	private final UserPasswordService userPasswordService;

	private final RoleService roleService;

	private final UserMapper userMapper;

	private final ApplicationEventPublisher eventPublisher;

	public UserController(UserService userService, UserPasswordService userPasswordService,
			RoleService roleService, UserMapper userMapper, ApplicationEventPublisher eventPublisher) {
		super();
		this.userService = userService;
		this.userPasswordService = userPasswordService;
		this.roleService = roleService;
		this.userMapper = userMapper;
		this.eventPublisher = eventPublisher;
	}

	@PreAuthorize("hasAnyAuthority('ADMIN', 'ADMIN_APP')")
	@GetMapping(value = "/page")
	public ResponseEntity<Page<PageableUsers>> getUsers(
			@PageableDefault(page = 0, size = 20) @SortDefault.SortDefaults({
					@SortDefault(sort = "username", direction = Sort.Direction.ASC) }) Pageable pageable) {
		Page<PageableUsers> pageUser = userService.pegeableUsers(pageable);
		LOG.debug("{}", pageUser);
		return ResponseEntity.ok().body(pageUser);
	}

	@PreAuthorize("hasAnyAuthority('ADMIN', 'ADMIN_APP')")
	@PatchMapping(value = "/{id}")
	public ResponseEntity<Boolean> changeStatusAccount(@PathVariable("id") Integer id,
			@RequestParam(value = "status", required = true) Boolean status) {
		userService.changeStatusAccount(id, status);
		LOG.debug("{}", "User deactivated");
		return ResponseEntity.ok().body(true);
	}

}