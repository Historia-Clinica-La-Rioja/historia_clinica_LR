package net.pladema.emergencycare.repository;

import net.pladema.emergencycare.repository.entity.HistoricEmergencyEpisode;
import net.pladema.emergencycare.repository.entity.HistoricEmergencyEpisodePK;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface HistoricEmergencyEpisodeRepository extends JpaRepository<HistoricEmergencyEpisode, HistoricEmergencyEpisodePK> {}