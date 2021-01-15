package net.pladema.emergencycare.repository;

import net.pladema.emergencycare.repository.domain.EmergencyCareVo;
import net.pladema.emergencycare.repository.entity.EmergencyCareEpisode;
import net.pladema.emergencycare.repository.entity.EmergencyCareState;
import net.pladema.emergencycare.service.domain.enums.EEmergencyCareState;
import net.pladema.sgx.masterdata.repository.MasterDataProjection;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Optional;

@Repository
public interface EmergencyCareEpisodeRepository extends JpaRepository<EmergencyCareEpisode, Integer> {

	@Transactional(readOnly = true)
	@Query(value = " SELECT NEW net.pladema.emergencycare.repository.domain.EmergencyCareVo(ece, pe.firstName, pe.lastName, dso.description) "+
			" FROM EmergencyCareEpisode ece "+
			" JOIN Patient pa ON (pa.id = ece.patientId) "+
			" JOIN Person pe ON (pe.id = pa.personId) "+
			" JOIN DoctorsOffice dso ON (dso.id = ece.doctorsOfficeId) "+
			" WHERE ece.emergencyCareStateId =  "+EmergencyCareState.EN_ATENCION+" OR ece.emergencyCareStateId = "+EmergencyCareState.EN_ESPERA+
			" AND ece.institutionId = :institutionId ")
	List<EmergencyCareVo> getAll(@Param("institutionId") Integer institutionId);

	@Transactional(readOnly = true)
	@Query(value = " SELECT NEW net.pladema.emergencycare.repository.domain.EmergencyCareVo(ece, pe.firstName, pe.lastName, dso.description) "+
			" FROM EmergencyCareEpisode ece "+
			" LEFT JOIN Patient pa ON (pa.id = ece.patientId) "+
			" LEFT JOIN Person pe ON (pe.id = pa.personId) "+
			" LEFT JOIN DoctorsOffice dso ON (dso.id = ece.doctorsOfficeId) "+
			" WHERE ece.id = :episodeId "+
			" AND ece.institutionId = :institutionId ")
	Optional<EmergencyCareVo> getEpisode(@Param("episodeId") Integer episodeId, @Param("institutionId") Integer institutionId);

	@Transactional(readOnly = true)
	@Query(value =" SELECT ece.emergency_care_state_id " +
			" FROM emergency_care_episode ece "+
			" WHERE ece.id = :episodeId "+
			" AND ece.institution_id = :institutionId ", nativeQuery = true)
	Optional<Short> getState(@Param("episodeId") Integer episodeId, @Param("institutionId") Integer institutionId);
}
