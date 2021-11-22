package net.pladema.permissions.repository;

import ar.lamansys.sgx.shared.auditable.repository.SGXAuditableEntityJPARepository;

import org.springframework.stereotype.Repository;

import net.pladema.permissions.repository.entity.Permission;

@Repository
public interface PermissionRepository extends SGXAuditableEntityJPARepository<Permission, Short> {

}
