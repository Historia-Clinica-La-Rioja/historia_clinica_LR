package net.pladema.establishment.repository;

import ar.lamansys.sgx.shared.auditable.repository.SGXAuditableEntityJPARepository;
import net.pladema.establishment.repository.entity.CareLine;
import org.springframework.stereotype.Repository;

@Repository
public interface CareLineRepository extends SGXAuditableEntityJPARepository<CareLine, Integer> {

}
