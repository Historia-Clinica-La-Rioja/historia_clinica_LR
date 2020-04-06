package net.pladema.permissions.controller;

import io.swagger.annotations.Api;
import net.pladema.permissions.controller.dto.RequestUserRoleDto;
import net.pladema.permissions.service.RoleService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import javax.validation.Valid;
import java.net.URI;
import java.net.URISyntaxException;

@RestController
@RequestMapping("/roles")
@Api(value = "Role", tags = { "Role" })
public class RoleController {

	private static final Logger LOG = LoggerFactory.getLogger(RoleController.class);

	private final RoleService roleService;

	public RoleController(RoleService licenseService) {
		super();
		this.roleService = licenseService;
	}

	@PreAuthorize("hasAnyAuthority('ADMIN', 'ADMIN_APP')")
	@PostMapping
	public ResponseEntity<Boolean> setRoleUser(@Valid @RequestBody RequestUserRoleDto userRoleDto)
			throws URISyntaxException {
		LOG.debug("{}", "UserRoleDto valid");
		roleService.deleteByUserId(userRoleDto.getUserId());
		roleService.createUserRole(userRoleDto.getUserId(), userRoleDto.getRoleId());
		return ResponseEntity.created(new URI("")).body(null);
	}

}