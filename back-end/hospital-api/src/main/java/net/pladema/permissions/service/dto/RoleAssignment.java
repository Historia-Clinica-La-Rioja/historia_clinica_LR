package net.pladema.permissions.service.dto;

public class RoleAssignment {
	public final String role;
	public final Integer institutionId;

	public RoleAssignment(String role, Integer institutionId) {
		this.role = role;
		this.institutionId = institutionId;
	}
}
