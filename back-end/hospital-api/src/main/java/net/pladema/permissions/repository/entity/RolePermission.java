package net.pladema.permissions.repository.entity;

import javax.persistence.EmbeddedId;
import javax.persistence.Entity;
import javax.persistence.EntityListeners;
import javax.persistence.Table;

import ar.lamansys.sgx.shared.auditable.entity.SGXAuditableEntity;
import ar.lamansys.sgx.shared.auditable.listener.SGXAuditListener;

@Entity
@Table(name = "role_permission")
@EntityListeners(SGXAuditListener.class)
public class RolePermission extends SGXAuditableEntity<RolePermissionPK> {

	/**
	 * 
	 */
	private static final long serialVersionUID = -4390270413353903503L;
	
	@EmbeddedId
	public RolePermissionPK rolePermissionPK;
	
	public RolePermission() {
		
	}
	
	public RolePermission(Short roleId, Short permissionId) {
		rolePermissionPK = new RolePermissionPK(roleId, permissionId);
	}
	
	public RolePermissionPK getRolePermissionPK() {
		return rolePermissionPK;
	}
	
	public Short getRoleId() {
		return rolePermissionPK.getRoleId();
	}
	
	public Short getPermissionId() {
		return rolePermissionPK.getPermissionId();
	}

	@Override
	public RolePermissionPK getId() {
		return this.rolePermissionPK;
	}
}
