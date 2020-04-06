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
public class UserRolePK implements Serializable {

	/**
	 * 
	 */
	private static final long serialVersionUID = -5482098137325590681L;

	@Column(name = "user_id", nullable = false)
	private Integer userId;

	@Column(name = "role_id", nullable = false)
	private Short roleId;

	public UserRolePK(Integer userId, Short roleId) {
		super();
		this.userId = userId;
		this.roleId = roleId;
	}

	@Override
	public int hashCode() {
		return Objects.hash(roleId, userId);
	}

	@Override
	public boolean equals(Object obj) {
		if (this == obj)
			return true;
		if (obj == null)
			return false;
		if (getClass() != obj.getClass())
			return false;
		UserRolePK other = (UserRolePK) obj;
		return Objects.equals(roleId, other.roleId) && Objects.equals(userId, other.userId);
	}

}
