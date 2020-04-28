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
@RequestMapping("/account")
public class LoggedUserController {
	private final Logger logger;
	private final LoggedUserService loggedUserService;

	public LoggedUserController(
			LoggedUserService loggedUserService
	) {
		this.logger = LoggerFactory.getLogger(this.getClass());
		this.loggedUserService = loggedUserService;
	}
	@GetMapping(value = "/permissions")
	public ResponseEntity<PermissionsDto> getPermissions() {
		PermissionsDto result = new PermissionsDto(loggedUserService.getPermissionAssignment());
		logger.debug("Output -> {}", result);
		return ResponseEntity.ok(result);
	}
}
