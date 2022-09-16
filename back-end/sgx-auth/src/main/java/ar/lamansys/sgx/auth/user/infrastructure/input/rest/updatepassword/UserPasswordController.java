package ar.lamansys.sgx.auth.user.infrastructure.input.rest.updatepassword;

import ar.lamansys.sgx.auth.user.application.updateownpassword.UpdateOwnPassword;
import ar.lamansys.sgx.auth.user.infrastructure.input.rest.updatepassword.dto.PasswordDto;
import ar.lamansys.sgx.shared.security.UserInfo;
import io.swagger.v3.oas.annotations.tags.Tag;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.HttpStatus;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.PatchMapping;
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

	private final UpdateOwnPassword updateOwnPassword;

	public UserPasswordController(UpdateOwnPassword updateOwnPassword) {
		this.logger = LoggerFactory.getLogger(getClass());
		this.updateOwnPassword = updateOwnPassword;
	}

	@PatchMapping
	@ResponseStatus(HttpStatus.OK)
	public void updatePassword(@NotNull @RequestBody PasswordDto passwordDto) {
		logger.debug("{}", "updatePassword valid");
		var userId = UserInfo.getCurrentAuditor();
		updateOwnPassword.execute(userId, passwordDto.getPassword(), passwordDto.getNewPassword());
		logger.debug("{}", "Password updated");
	}
}