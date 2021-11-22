package net.pladema.permissions.repository;

import ar.lamansys.sgx.shared.auditable.repository.SGXAuditableEntityJPARepository;

import org.springframework.stereotype.Repository;

import net.pladema.permissions.repository.entity.RolePermission;
import net.pladema.permissions.repository.entity.RolePermissionPK;

@Repository
public interface RolePermissionRepository extends SGXAuditableEntityJPARepository<RolePermission, RolePermissionPK> {

}
