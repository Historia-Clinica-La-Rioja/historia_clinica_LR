package net.pladema.emergencycare.repository;

import ar.lamansys.sgx.shared.auditable.repository.SGXAuditableEntityJPARepository;
import net.pladema.emergencycare.repository.entity.HistoricEmergencyEpisode;
import net.pladema.emergencycare.repository.entity.HistoricEmergencyEpisodePK;

import org.springframework.stereotype.Repository;

@Repository
public interface HistoricEmergencyEpisodeRepository extends SGXAuditableEntityJPARepository<HistoricEmergencyEpisode, HistoricEmergencyEpisodePK> {}