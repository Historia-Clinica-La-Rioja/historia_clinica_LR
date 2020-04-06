package net.pladema.permissions.repository.entity;

import java.io.Serializable;
import java.util.Objects;

import javax.persistence.Column;
import javax.persistence.Embeddable;

import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Embeddable
@Getter
@Setter
@NoArgsConstructor
public class RolePermissionPK implements Serializable {

	/**
	 * 
	 */
	private static final long serialVersionUID = 5877875325836061171L;

	@Column(name = "role_id", nullable = false)
	private Short roleId;

	@Column(name = "permission_id", nullable = false)
	private Short permissionId;

	public RolePermissionPK(Short roleId, Short permissionId) {
		super();
		this.roleId = roleId;
		this.permissionId = permissionId;
	}

	@Override
	public int hashCode() {
		return Objects.hash(roleId, permissionId);
	}

	@Override
	public boolean equals(Object obj) {
		if (this == obj)
			return true;
		if (obj == null)
			return false;
		if (getClass() != obj.getClass())
			return false;
		RolePermissionPK other = (RolePermissionPK) obj;
		return Objects.equals(roleId, other.roleId) && Objects.equals(permissionId, other.permissionId);
	}

}
