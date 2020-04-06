package net.pladema.user.controller.constraints;

import javax.validation.ConstraintValidator;
import javax.validation.ConstraintValidatorContext;

import net.pladema.user.repository.entity.User;
import net.pladema.user.service.UserService;

public class ValidUserValidator implements ConstraintValidator<ValidUser, Integer> {

	private final UserService userService;

	public ValidUserValidator(UserService userService) {
		super();
		this.userService = userService;
	}

	@Override
	public boolean isValid(Integer userId, ConstraintValidatorContext context) {
		if (userId == null)
			return false;
		User admin = userService.getAdminUser();
		if (admin != null && userId.equals(admin.getId()))
			return false;
		return userService.existUser(userId);
	}

}
