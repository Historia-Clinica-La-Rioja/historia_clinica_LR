package ar.lamansys.sgh.clinichistory.infrastructure.output.repository.ips;

import ar.lamansys.sgh.clinichistory.infrastructure.output.repository.ips.entity.AllergyIntolerance;
import ar.lamansys.sgx.shared.auditable.repository.SGXAuditableEntityJPARepository;
import org.springframework.stereotype.Repository;

@Repository
public interface AllergyIntoleranceRepository extends SGXAuditableEntityJPARepository<AllergyIntolerance, Integer> {

}
