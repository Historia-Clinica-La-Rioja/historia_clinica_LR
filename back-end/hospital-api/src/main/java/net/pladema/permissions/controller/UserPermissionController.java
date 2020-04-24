package net.pladema.permissions.controller;

import net.pladema.permissions.controller.dto.PermissionsDto;
import net.pladema.permissions.service.LoggedUserService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/permissions")
public class UserPermissionController {

	private static final Logger LOG = LoggerFactory.getLogger(UserPermissionController.class);

	private final LoggedUserService loggedUserService;

	public UserPermissionController(LoggedUserService loggedUserService) {
		this.loggedUserService = loggedUserService;
	}

	@GetMapping
	public ResponseEntity<PermissionsDto> getPermissions() {
		PermissionsDto result = new PermissionsDto(loggedUserService.getPermissionAssignment());
		LOG.debug("Output -> {}", result);
		return ResponseEntity.ok(result);
	}
}
