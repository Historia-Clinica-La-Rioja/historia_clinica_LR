package net.pladema.permissions.service.dto;

import java.io.Serializable;

import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.ToString;
import net.pladema.permissions.repository.enums.ERole;

@EqualsAndHashCode
@Getter
@ToString
public class RoleAssignment implements Serializable{
	
	private static final long serialVersionUID = 4400712415700752391L;
	
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
