package net.pladema.permissions.service.dto;

import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.ToString;

@EqualsAndHashCode
@Getter
@ToString
public class RoleAssignment {
	public final String role;
	public final Integer institutionId;

	public RoleAssignment(String role, Integer institutionId) {
		this.role = role;
		this.institutionId = institutionId;
	}
}
