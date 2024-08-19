package ar.lamansys.sgh.clinichistory.infrastructure.output.repository.ips;

import ar.lamansys.sgh.clinichistory.infrastructure.output.repository.ips.entity.FhirObservationGroup;
import ar.lamansys.sgx.shared.auditable.repository.SGXAuditableEntityJPARepository;

import org.springframework.stereotype.Repository;

@Repository
public interface FhirObservationGroupRepository extends SGXAuditableEntityJPARepository<FhirObservationGroup, Integer> {
}
