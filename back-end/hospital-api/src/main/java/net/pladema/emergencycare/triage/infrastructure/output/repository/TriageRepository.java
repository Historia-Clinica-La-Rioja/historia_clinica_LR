package net.pladema.emergencycare.triage.infrastructure.output.repository;

import ar.lamansys.sgx.shared.auditable.repository.SGXAuditableEntityJPARepository;
import net.pladema.emergencycare.triage.repository.TriageRepositoryCustom;
import net.pladema.emergencycare.triage.infrastructure.output.entity.Triage;

import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface TriageRepository extends SGXAuditableEntityJPARepository<Triage, Integer>, TriageRepositoryCustom {

	List<Triage> findAllByEmergencyCareEpisodeIdOrderByIdDesc(@Param("emergencyCareEpisodeId") Integer emergencyCareEpisodeId);
}