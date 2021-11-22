package ar.lamansys.sgx.auth.user.infrastructure.input.rest.passwordreset;

import ar.lamansys.sgx.auth.user.application.resetpassword.ResetPassword;
import ar.lamansys.sgx.auth.user.infrastructure.input.rest.passwordreset.dto.PasswordResetDto;
import ar.lamansys.sgx.auth.user.infrastructure.input.rest.passwordreset.dto.PasswordResetResponseDto;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;

@RestController
@RequestMapping("password-reset")
public class PasswordResetController {

	private final Logger logger;

	private final ResetPassword resetPassword;

	public PasswordResetController( ResetPassword resetPassword) {
		this.resetPassword = resetPassword;
		this.logger = LoggerFactory.getLogger(this.getClass());
	}


	@PostMapping
	@ResponseStatus(HttpStatus.OK)
	public PasswordResetResponseDto reset(@Valid @RequestBody PasswordResetDto passwordResetDto) {
		logger.debug("Reset password to token {} ", passwordResetDto.getToken());
		String result =  resetPassword.execute(passwordResetDto.getToken(), passwordResetDto.getPassword());
		logger.debug("Username {} -> password reseted", result);
		return new PasswordResetResponseDto(result);
	}
}
