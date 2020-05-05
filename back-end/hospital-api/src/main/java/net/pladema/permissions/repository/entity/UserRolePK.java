package net.pladema.permissions.repository.entity;

import java.io.Serializable;

import javax.persistence.Column;
import javax.persistence.Embeddable;

import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import net.pladema.permissions.repository.enums.ERole;

@Embeddable
@Getter
@Setter
@NoArgsConstructor
@EqualsAndHashCode
/**
 * Para el caso de los roles donde ERole.isAdmin == true, la institución es -1.
 * Esos son roles de administración que no están asociados a ninguna institución.
 */
public class UserRolePK implements Serializable {

	public UserRolePK(Integer userId, Short roleId) {
		this(userId, roleId, -1);
	}

	public UserRolePK(Integer userId, Short roleId, Integer institutionId) {
		this.userId = userId;
		this.roleId = roleId;
		if (institutionId == null || ERole.map(roleId).getIsAdmin()) 
			this.institutionId = -1;
		else
			this.institutionId = institutionId;
	}
	
	private static final long serialVersionUID = -5482098137325590681L;

	@Column(name = "user_id", nullable = false)
	private Integer userId;

	@Column(name = "role_id", nullable = false)
	private Short roleId;
	
	@Column(name = "institution_id", nullable = false)
	private Integer institutionId;

}
