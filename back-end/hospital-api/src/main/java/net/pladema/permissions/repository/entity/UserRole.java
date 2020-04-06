package net.pladema.permissions.repository.entity;

import javax.persistence.EmbeddedId;
import javax.persistence.Entity;
import javax.persistence.EntityListeners;
import javax.persistence.Table;

import net.pladema.auditable.entity.AuditableEntity;
import net.pladema.auditable.listener.AuditListener;

@Entity
@Table(name = "user_role")
@EntityListeners(AuditListener.class)
public class UserRole extends AuditableEntity {

	/**
	 * 
	 */
	private static final long serialVersionUID = -2826874188803670205L;

	@EmbeddedId
	public UserRolePK userRolePK;

	public UserRole() {

	}

	public UserRole(Integer userId, Short roleId) {
		userRolePK = new UserRolePK(userId, roleId);
	}

	public UserRolePK getUserRolePK() {
		return userRolePK;
	}

	public Integer getUserId() {
		return userRolePK.getUserId();
	}

	public Short getRoleId() {
		return userRolePK.getRoleId();
	}

}
