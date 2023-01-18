package net.pladema.patient.repository;

import org.springframework.stereotype.Repository;

import ar.lamansys.sgx.shared.auditable.repository.SGXAuditableEntityJPARepository;
import net.pladema.patient.repository.entity.MergedPatient;

@Repository
public interface MergedPatientRepository extends SGXAuditableEntityJPARepository<MergedPatient, Integer> {


}