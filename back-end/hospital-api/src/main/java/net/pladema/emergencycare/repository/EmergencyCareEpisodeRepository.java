package net.pladema.emergencycare.repository;

import ar.lamansys.sgx.shared.auditable.repository.SGXAuditableEntityJPARepository;
import net.pladema.emergencycare.repository.domain.EmergencyCareVo;
import net.pladema.emergencycare.repository.entity.EmergencyCareEpisode;
import net.pladema.emergencycare.repository.entity.EmergencyCareState;

import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Optional;

@Repository
public interface EmergencyCareEpisodeRepository extends SGXAuditableEntityJPARepository<EmergencyCareEpisode, Integer> {

	@Transactional(readOnly = true)
	@Query(value = " SELECT NEW net.pladema.emergencycare.repository.domain.EmergencyCareVo(ece, pe, pa.typeId, " +
			"petd.nameSelfDetermination, dso.description, tc) "+
			" FROM EmergencyCareEpisode ece "+
			" LEFT JOIN Patient pa ON (pa.id = ece.patientId) "+
			" LEFT JOIN Person pe ON (pe.id = pa.personId) "+
			" LEFT JOIN DoctorsOffice dso ON (dso.id = ece.doctorsOfficeId) "+
			" LEFT JOIN PersonExtended petd ON (pe.id = petd.id) "+
			" JOIN TriageCategory tc ON (tc.id = ece.triageCategoryId) "+
			" WHERE (ece.emergencyCareStateId = " + EmergencyCareState.EN_ATENCION +
				" OR ece.emergencyCareStateId = " + EmergencyCareState.EN_ESPERA +
				" OR ece.emergencyCareStateId = " + EmergencyCareState.CON_ALTA_MEDICA + " ) "+
			" AND ece.institutionId = :institutionId ")
	List<EmergencyCareVo> getAll(@Param("institutionId") Integer institutionId);

	@Transactional(readOnly = true)
	@Query( value = "SELECT ece.id " +
			" FROM EmergencyCareEpisode ece " +
			" WHERE ece.patientId = :patientId AND ( ece.emergencyCareStateId = " + EmergencyCareState.EN_ESPERA +
			" OR ece.emergencyCareStateId = " + EmergencyCareState.EN_ATENCION +
			" OR ece.emergencyCareStateId = " + EmergencyCareState.CON_ALTA_MEDICA + " ) " +
			" AND ece.institutionId = :institutionId " +
			" GROUP BY ece.id ")
	Optional<Integer> emergencyCareEpisodeInProgress(@Param("institutionId") Integer institutionId, @Param("patientId") Integer patientId);

	@Transactional(readOnly = true)
	@Query(value = " SELECT NEW net.pladema.emergencycare.repository.domain.EmergencyCareVo(ece, pe, pa.typeId, petd.nameSelfDetermination, dso.description, tc, pi) "+
			" FROM EmergencyCareEpisode ece "+
			" LEFT JOIN Patient pa ON (pa.id = ece.patientId) "+
			" LEFT JOIN Person pe ON (pe.id = pa.personId) " +
			" LEFT JOIN PersonExtended petd ON (pe.id = petd.id) "+
			" LEFT JOIN DoctorsOffice dso ON (dso.id = ece.doctorsOfficeId) "+
			" LEFT JOIN PoliceInterventionDetails pi ON (pi.id = ece.id) "+
			" JOIN TriageCategory tc ON (tc.id = ece.triageCategoryId) "+
			" WHERE ece.id = :episodeId "+
			" AND ece.institutionId = :institutionId ")
	Optional<EmergencyCareVo> getEpisode(@Param("episodeId") Integer episodeId, @Param("institutionId") Integer institutionId);

	@Transactional(readOnly = true)
	@Query(value =" SELECT ece.emergency_care_state_id " +
			" FROM emergency_care_episode ece "+
			" WHERE ece.id = :episodeId "+
			" AND ece.institution_id = :institutionId ", nativeQuery = true)
	Optional<Short> getState(@Param("episodeId") Integer episodeId, @Param("institutionId") Integer institutionId);

	@Transactional
	@Modifying
	@Query(value = " UPDATE EmergencyCareEpisode AS ece " +
			" SET ece.emergencyCareStateId = :emergencyCareStateId, " +
			" ece.doctorsOfficeId = :doctorsOfficeId, " +
			" ece.updateable.updatedOn = CURRENT_TIMESTAMP " +
			" WHERE ece.id = :episodeId "+
			" AND ece.institutionId = :institutionId ")
	void updateState(@Param("episodeId") Integer episodeId,
					 @Param("institutionId") Integer institutionId,
					 @Param("emergencyCareStateId") Short emergencyCareStateId,
					 @Param("doctorsOfficeId") Integer doctorsOfficeId);

	@Transactional
	@Modifying				 
	@Query(value = "UPDATE EmergencyCareEpisode AS ece SET ece.patientId = :patientId WHERE ece.id = :episodeId")
	void updatePatientId(@Param("episodeId") Integer episodeId, @Param("patientId") Integer patientId);

	@Transactional
	@Modifying
	@Query(value = "UPDATE EmergencyCareEpisode AS ece SET ece.triageCategoryId = :triageCategoryId WHERE ece.id = :episodeId")
	void updateTriageCategoryId(@Param("episodeId") Integer episodeId, @Param("triageCategoryId") Short triageCategoryId);

	@Transactional(readOnly = true)
	@Query( value = "SELECT  (case when count(ece.id)> 0 then true else false end) " +
			"FROM EmergencyCareEpisode ece " +
			"WHERE ece.patientId = :patientId AND ( ece.emergencyCareStateId =" + EmergencyCareState.EN_ESPERA +
				" or ece.emergencyCareStateId = " + EmergencyCareState.EN_ATENCION +
				" or ece.emergencyCareStateId = " + EmergencyCareState.CON_ALTA_MEDICA + " ) " +
			"AND ece.institutionId = :institutionId")
	boolean existsActiveEpisodeByPatientIdAndInstitutionId(@Param("patientId") Integer patientId, @Param("institutionId") Integer institutionId);
}
