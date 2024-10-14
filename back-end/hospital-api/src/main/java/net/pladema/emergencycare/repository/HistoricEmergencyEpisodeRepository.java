package net.pladema.emergencycare.repository;

import ar.lamansys.sgx.shared.auditable.repository.SGXAuditableEntityJPARepository;
import net.pladema.emergencycare.repository.entity.HistoricEmergencyEpisode;
import net.pladema.emergencycare.repository.entity.HistoricEmergencyEpisodePK;
import net.pladema.emergencycare.service.domain.EmergencyEpisodePatientBedRoomBo;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;

@Repository
public interface HistoricEmergencyEpisodeRepository extends SGXAuditableEntityJPARepository<HistoricEmergencyEpisode, HistoricEmergencyEpisodePK> {

	@Transactional(readOnly = true)
	@Query("SELECT NEW net.pladema.emergencycare.service.domain.EmergencyEpisodePatientBedRoomBo(b.bedNumber, r.roomNumber) " +
			"FROM HistoricEmergencyEpisode hee " +
			"LEFT JOIN Bed b ON hee.bedId = b.id " +
			"LEFT JOIN Room r ON b.roomId = r.id " +
			"WHERE hee.pk.changeStateDate <= :requestDate " +
			"AND hee.pk.emergencyCareEpisodeId = :emergencyId " +
			"ORDER BY hee.pk.changeStateDate DESC")
	Page<EmergencyEpisodePatientBedRoomBo> getLastBedByEmergencyEpisodePatientDate(@Param("emergencyId") Integer emergencyId,
																				   @Param("requestDate") LocalDateTime requestDate,
																				   Pageable pageable);

	List<HistoricEmergencyEpisode> findAllByPk_EmergencyCareEpisodeId(Integer emergencyEpisodeId);

	@Query(value = "SELECT * FROM historic_emergency_episode WHERE emergency_care_episode_id = :episodeId ORDER BY change_state_date DESC LIMIT 1", nativeQuery = true)
	Optional<HistoricEmergencyEpisode> findLatestByEmergencyCareEpisodeId(@Param("episodeId") Integer episodeId);
}