package net.pladema.patient.infrastructure.output.repository;

import ar.lamansys.sgx.shared.auditable.repository.SGXAuditableEntityJPARepository;
import net.pladema.patient.infrastructure.output.repository.entity.MergedPatientItem;

import org.springframework.stereotype.Repository;

@Repository
public interface MergedPatientItemRepository extends SGXAuditableEntityJPARepository<MergedPatientItem, Integer> {

}
