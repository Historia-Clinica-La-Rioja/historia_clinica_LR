package net.pladema.permissions.controller.constraints;

import javax.validation.ConstraintValidator;
import javax.validation.ConstraintValidatorContext;

import net.pladema.permissions.service.RoleService;

public class ExistRoleValidator implements ConstraintValidator<ExistRole, Short> {

	private final RoleService roleService;

	public ExistRoleValidator(RoleService roleService) {
		super();
		this.roleService = roleService;
	}

	@Override
	public boolean isValid(Short id, ConstraintValidatorContext context) {
		return (id != null && (id.compareTo((short) 1) > 0) && roleService.existRole(id));
	}
}
