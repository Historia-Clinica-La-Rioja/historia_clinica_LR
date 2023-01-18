package net.pladema.patient.repository;

import ar.lamansys.sgx.shared.auditable.repository.SGXAuditableEntityJPARepository;
import net.pladema.patient.repository.entity.MergedInactivePatient;
import net.pladema.patient.repository.entity.MergedPatient;

import org.springframework.stereotype.Repository;

@Repository
public interface MergedInactivePatientRepository extends SGXAuditableEntityJPARepository<MergedInactivePatient, Integer> {


}