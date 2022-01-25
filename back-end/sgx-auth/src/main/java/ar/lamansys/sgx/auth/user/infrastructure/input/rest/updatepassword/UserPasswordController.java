package ar.lamansys.sgx.auth.user.infrastructure.input.rest.updatepassword;

import ar.lamansys.sgx.auth.user.application.updatepassword.UpdatePassword;
import ar.lamansys.sgx.auth.user.domain.user.model.UserBo;
import ar.lamansys.sgx.auth.user.domain.user.service.UserStorage;
import io.swagger.v3.oas.annotations.tags.Tag;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.HttpStatus;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.PatchMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestController;

import javax.validation.constraints.NotNull;

@RestController
@RequestMapping("/passwords")
@Tag(name = "Password", description = "Password")
@Validated
public class UserPasswordController {

	private final Logger logger;

	private final UpdatePassword updatePassword;

	private final UserStorage userStorage;

	public UserPasswordController(UpdatePassword updatePassword,
								  UserStorage userStorage) {
		this.logger = LoggerFactory.getLogger(getClass());
		this.updatePassword = updatePassword;
		this.userStorage = userStorage;
	}

	@PreAuthorize("#userId == authentication.principal")
	@PatchMapping(value = "/{userId}")
	@ResponseStatus(HttpStatus.OK)
	public void updatePassword(@PathVariable("userId") Integer userId,
			@NotNull @RequestBody String password) {
		logger.debug("{}", "updatePassword valid");
		UserBo user = userStorage.getUser(userId);
		updatePassword.execute(user.getUsername(), password);
		logger.debug("{}", "Password updated");
	}
}