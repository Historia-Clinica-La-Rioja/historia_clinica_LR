package net.pladema.permissions.repository.entity;

import javax.persistence.Column;
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
	
	@Column(name = "institution_id")
	private Integer institutionId;

	public UserRole() {

	}

	public UserRole(Integer userId, Short roleId) {
		userRolePK = new UserRolePK(userId, roleId);
	}
	
	public UserRole(Integer userId, Short roleId, Integer institutionId) {
		this(userId, roleId);
		this.institutionId = institutionId;
	}

	public UserRolePK getUserRolePK() {
		return userRolePK;
	}

	public Integer getUserId() {
		return userRolePK.getUserId();
	}
	
	public void setUserId(Integer userId) {
		if (this.userRolePK == null)
			this.userRolePK = new UserRolePK();
		this.userRolePK.setUserId(userId);
	}

	public void setRoleId(Short roleId) {
		if (this.userRolePK == null)
			this.userRolePK = new UserRolePK();
		this.userRolePK.setRoleId(roleId);
	}
	
	public Short getRoleId() {
		return userRolePK.getRoleId();
	}
	

	public Integer getInstitutionId() {
		return institutionId;
	}

	public void setInstitutionId(Integer institutionId) {
		this.institutionId = institutionId;
	}

	@Override
	public int hashCode() {
		final int prime = 31;
		int result = 1;
		result = prime * result + ((institutionId == null) ? 0 : institutionId.hashCode());
		result = prime * result + ((userRolePK == null) ? 0 : userRolePK.hashCode());
		return result;
	}

	@Override
	public boolean equals(Object obj) {
		if (this == obj)
			return true;
		if (obj == null)
			return false;
		if (getClass() != obj.getClass())
			return false;
		UserRole other = (UserRole) obj;
		if (institutionId == null) {
			if (other.institutionId != null)
				return false;
		} else if (!institutionId.equals(other.institutionId))
			return false;
		if (userRolePK == null) {
			if (other.userRolePK != null)
				return false;
		} else if (!userRolePK.equals(other.userRolePK))
			return false;
		return true;
	}

}
