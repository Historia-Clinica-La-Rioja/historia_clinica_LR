package net.pladema.person.repository;

import ar.lamansys.sgx.shared.auditable.repository.SGXAuditableEntityJPARepository;
import net.pladema.person.repository.entity.PersonFile;

import org.springframework.stereotype.Repository;

@Repository
public interface PersonFileRepository extends SGXAuditableEntityJPARepository<PersonFile, Integer> {
}
