package net.pladema.permissions.controller;

import net.pladema.permissions.controller.dto.PermissionsDto;
import net.pladema.permissions.controller.mappers.UserInfoMapper;
import net.pladema.permissions.service.LoggedUserService;
import net.pladema.permissions.service.domain.UserBo;
import net.pladema.person.controller.dto.BasicDataPersonDto;
import net.pladema.person.controller.service.PersonExternalService;
import net.pladema.user.controller.dto.UserDto;
import net.pladema.user.controller.dto.UserPersonDto;
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
	private final PersonExternalService personExternalService;
	private final UserInfoMapper userInfoMapper;

	public LoggedUserController(
			LoggedUserService loggedUserService,
			PersonExternalService personExternalService, UserInfoMapper userInfoMapper) {
		this.personExternalService = personExternalService;
		this.userInfoMapper = userInfoMapper;
		this.logger = LoggerFactory.getLogger(this.getClass());
		this.loggedUserService = loggedUserService;
	}
	@GetMapping(value = "/permissions")
	public ResponseEntity<PermissionsDto> getPermissions() {
		PermissionsDto result = new PermissionsDto(loggedUserService.getPermissionAssignment());
		logger.debug("Output -> {}", result);
		return ResponseEntity.ok(result);
	}

	@GetMapping(value = "/info")
	public ResponseEntity<UserDto> getInfo() {
		UserBo userBo = loggedUserService.getInfo();
		BasicDataPersonDto personBasicData = personExternalService.getBasicDataPerson(userBo.getPersonId());
		UserPersonDto userPersonDto = userInfoMapper.toUserPersonDto(personBasicData);
		UserDto userDto = userInfoMapper.toUserDto(userPersonDto,userBo);
		return ResponseEntity.ok(userDto);
	}
}
