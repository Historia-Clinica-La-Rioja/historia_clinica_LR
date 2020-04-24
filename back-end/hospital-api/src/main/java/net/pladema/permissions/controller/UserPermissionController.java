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
	private final Logger logger;
	private final LoggedUserService loggedUserService;

	public UserPermissionController(
			LoggedUserService loggedUserService
	) {
		this.loggedUserService = loggedUserService;
		this.logger = LoggerFactory.getLogger(this.getClass());
	}
	@GetMapping
	public ResponseEntity<PermissionsDto> getPermissions() {
		return ResponseEntity.ok(new PermissionsDto(
				loggedUserService.getPermissionAssignment()
		));
	}
}
