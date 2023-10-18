package net.pladema.establishment.repository;

import ar.lamansys.sgx.shared.auditable.repository.SGXAuditableEntityJPARepository;
import net.pladema.establishment.repository.entity.InstitutionalGroupInstitution;

import net.pladema.establishment.repository.entity.InstitutionalGroupUser;

import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Repository
public interface InstitutionalGroupUserRepository extends SGXAuditableEntityJPARepository<InstitutionalGroupUser, Integer> {

}
