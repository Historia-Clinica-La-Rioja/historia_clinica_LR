package net.pladema.emergencycare.repository;

import ar.lamansys.sgx.shared.auditable.repository.SGXAuditableEntityJPARepository;
import net.pladema.emergencycare.repository.entity.EmergencyCareEvolutionNote;

import org.springframework.stereotype.Repository;

@Repository
public interface EmergencyCareEvolutionNoteRepository extends SGXAuditableEntityJPARepository<EmergencyCareEvolutionNote, Integer> {
}
