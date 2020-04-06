package net.pladema.security.authentication.constraints;

import javax.validation.ConstraintValidator;
import javax.validation.ConstraintValidatorContext;

import net.pladema.user.service.UserService;

public class EnabledUserValidator implements ConstraintValidator<EnabledUser, String> {

	private final UserService userService;

	public EnabledUserValidator(UserService userService) {
		super();
		this.userService = userService;
	}

	@Override
	public boolean isValid(String username, ConstraintValidatorContext context) {
		return (username != null && userService.isEnable(username));
	}

}

