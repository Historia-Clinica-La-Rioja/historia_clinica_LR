package net.pladema.sgx.loggeduser.infrastructure.input.rest;

import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import lombok.extern.slf4j.Slf4j;
import net.pladema.permissions.service.LoggedUserService;
import net.pladema.sgx.loggeduser.infrastructure.input.rest.dto.LoggedUserDto;
import net.pladema.sgx.loggeduser.infrastructure.input.rest.dto.PermissionsDto;

@Slf4j
@RestController
@RequestMapping("/account")
public class LoggedUserController {

	private final LoggedUserService loggedUserService;

	public LoggedUserController(
			LoggedUserService loggedUserService
	) {
		this.loggedUserService = loggedUserService;
	}

	@GetMapping(value = "/permissions")
	public PermissionsDto getPermissions() {
		PermissionsDto result = new PermissionsDto(loggedUserService.getPermissionAssignment());
		log.debug("Output -> {}", result);
		return result;
	}

	@GetMapping(value = "/info")
	public LoggedUserDto getInfo() {
		LoggedUserDto userDto = new LoggedUserDto(loggedUserService.getInfo());
		return userDto;
	}

}
