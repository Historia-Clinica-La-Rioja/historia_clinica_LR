package ar.lamansys.refcounterref.infraestructure.output.repository.forwarding;

import ar.lamansys.sgx.shared.auditable.repository.SGXAuditableEntityJPARepository;

import org.springframework.stereotype.Repository;

@Repository
public interface ReferenceForwardingRepository extends SGXAuditableEntityJPARepository<ReferenceForwarding, Integer> {
}
