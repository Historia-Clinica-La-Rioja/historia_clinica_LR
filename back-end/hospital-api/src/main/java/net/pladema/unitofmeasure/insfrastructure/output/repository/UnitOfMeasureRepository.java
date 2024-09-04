package net.pladema.unitofmeasure.insfrastructure.output.repository;

import ar.lamansys.sgx.shared.auditable.repository.SGXAuditableEntityJPARepository;
import net.pladema.unitofmeasure.insfrastructure.output.entity.UnitOfMeasure;

import org.springframework.stereotype.Repository;

@Repository
public interface UnitOfMeasureRepository extends SGXAuditableEntityJPARepository<UnitOfMeasure, Short> {
}
