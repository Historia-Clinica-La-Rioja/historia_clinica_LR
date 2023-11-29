package ar.lamansys.refcounterref.infraestructure.output.repository.referenceregulation;

import ar.lamansys.sgx.shared.auditable.repository.SGXAuditableEntityJPARepository;

import org.springframework.stereotype.Repository;

@Repository
public interface HistoricReferenceRegulationRepository extends SGXAuditableEntityJPARepository<HistoricReferenceRegulation, Integer> {

}
