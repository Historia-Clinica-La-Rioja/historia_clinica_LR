package net.pladema.permissions.repository;

import ar.lamansys.sgx.shared.auditable.repository.SGXAuditableEntityJPARepository;
import org.springframework.stereotype.Repository;

import net.pladema.permissions.repository.entity.Role;

@Repository
public interface RoleRepository extends SGXAuditableEntityJPARepository<Role, Short> {

}
