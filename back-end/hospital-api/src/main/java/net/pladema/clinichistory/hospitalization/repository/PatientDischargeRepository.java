package net.pladema.clinichistory.hospitalization.repository;

import ar.lamansys.sgx.shared.auditable.repository.SGXAuditableEntityJPARepository;
import net.pladema.clinichistory.hospitalization.repository.domain.PatientDischarge;

import org.springframework.stereotype.Repository;

@Repository
public interface PatientDischargeRepository extends SGXAuditableEntityJPARepository<PatientDischarge, Integer> {

}
