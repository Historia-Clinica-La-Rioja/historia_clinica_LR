package ar.lamansys.refcounterref.infraestructure.output.repository.referenceobservation;

import ar.lamansys.sgx.shared.auditable.repository.SGXAuditableEntityJPARepository;

import org.springframework.stereotype.Repository;

@Repository
public interface ReferenceObservationRepository extends SGXAuditableEntityJPARepository<ReferenceObservation, Integer> {
}
