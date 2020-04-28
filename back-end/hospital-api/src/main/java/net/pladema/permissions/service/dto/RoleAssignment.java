package net.pladema.permissions.service.dto;

import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.ToString;
import net.pladema.permissions.repository.entity.Role;
import net.pladema.permissions.repository.enums.ERole;

@EqualsAndHashCode
@Getter
@ToString
public class RoleAssignment {
	public final ERole role;
	public final Integer institutionId;

	public RoleAssignment(ERole role, Integer institutionId) {
		this.role = role;
		this.institutionId = institutionId;
	}

	public RoleAssignment(Short roleId, Integer institutionId) {
		this(ERole.map(roleId), institutionId);
	}

}
