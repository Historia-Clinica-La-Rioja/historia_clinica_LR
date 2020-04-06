package net.pladema.security.authentication.constraints;

import javax.validation.ConstraintValidator;
import javax.validation.ConstraintValidatorContext;

import net.pladema.security.token.service.TokenService;

public class VerificationTokenValidator implements ConstraintValidator<VerificationToken, String> {

	private final TokenService tokenService;

	public VerificationTokenValidator(TokenService tokenService) {
		super();
		this.tokenService = tokenService;
	}

	@Override
	public boolean isValid(String verificationToken, ConstraintValidatorContext context) {
		return (verificationToken != null && tokenService.validVerificationToken(verificationToken));
	}

}
