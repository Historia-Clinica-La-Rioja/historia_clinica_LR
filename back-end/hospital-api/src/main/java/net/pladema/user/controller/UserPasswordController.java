package net.pladema.user.controller;

import io.swagger.annotations.Api;
import net.pladema.security.service.SecurityService;
import net.pladema.user.service.UserPasswordService;
import net.pladema.user.service.UserService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.ApplicationEventPublisher;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

import javax.validation.constraints.NotNull;

@RestController
@RequestMapping("/passwords")
@Api(value = "Password", tags = { "Password" })
@Validated
public class UserPasswordController {

	private static final Logger LOG = LoggerFactory.getLogger(UserPasswordController.class);

	private static final String USER_VALID = "User valid";

	@Value("${frontend.loginpage}")
	protected String loginPage;

	private final UserService userService;

	private final UserPasswordService userPasswordService;

	private final ApplicationEventPublisher eventPublisher;

	private final SecurityService securityService;

	public UserPasswordController(UserService userService, UserPasswordService userPasswordService,
			ApplicationEventPublisher eventPublisher, SecurityService securityService) {
		super();
		this.userService = userService;
		this.userPasswordService = userPasswordService;
		this.eventPublisher = eventPublisher;
		this.securityService = securityService;
	}

	@PreAuthorize("#userId == authentication.principal")
	@PatchMapping(value = "/{userId}")
	public ResponseEntity<Boolean> updatePassword(@PathVariable("userId") Integer userId,
			@NotNull @RequestBody String password) {
		LOG.debug("{}", "updatePassword valid");
		userPasswordService.updatePassword(userId, password);
		LOG.debug("{}", "Password updated");
		return ResponseEntity.ok().body(true);
	}
}