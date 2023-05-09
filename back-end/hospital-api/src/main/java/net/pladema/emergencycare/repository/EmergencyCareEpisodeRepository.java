package net.pladema.emergencycare.repository;

import ar.lamansys.sgh.clinichistory.infrastructure.output.repository.ips.entity.ObservationRiskFactor;
import ar.lamansys.sgx.shared.auditable.repository.SGXAuditableEntityJPARepository;
import net.pladema.emergencycare.repository.domain.EmergencyCareVo;
import net.pladema.emergencycare.repository.domain.PatientECEVo;
import net.pladema.emergencycare.repository.domain.ProfessionalPersonVo;
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
			"petd.nameSelfDetermination, dso.description, tc, s.description, b) "+
			" FROM EmergencyCareEpisode ece "+
			" LEFT JOIN Patient pa ON (pa.id = ece.patientId) "+
			" LEFT JOIN Person pe ON (pe.id = pa.personId) "+
			" LEFT JOIN DoctorsOffice dso ON (dso.id = ece.doctorsOfficeId) "+
			" LEFT JOIN PersonExtended petd ON (pe.id = petd.id) "+
			" JOIN TriageCategory tc ON (tc.id = ece.triageCategoryId) " +
			" LEFT JOIN Shockroom s ON (s.id = ece.shockroomId)" +
			" LEFT JOIN Bed b ON (ece.bedId = b.id) " +
			" WHERE (ece.emergencyCareStateId = " + EmergencyCareState.EN_ATENCION +
				" OR ece.emergencyCareStateId = " + EmergencyCareState.EN_ESPERA +
				" OR ece.emergencyCareStateId = " + EmergencyCareState.CON_ALTA_MEDICA +
				" OR ece.emergencyCareStateId = " + EmergencyCareState.CON_ALTA_ADMINISTRATIVA + " ) " +
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
	@Query(value = " SELECT NEW net.pladema.emergencycare.repository.domain.EmergencyCareVo(ece, pe, pa.typeId, petd.nameSelfDetermination, dso.description, tc, pi, s.description, b, ecd.administrativeDischargeOn) "+
			" FROM EmergencyCareEpisode ece "+
			" LEFT JOIN EmergencyCareDischarge ecd ON (ecd.emergencyCareEpisodeId = ece.id) " +
			" LEFT JOIN Patient pa ON (pa.id = ece.patientId) "+
			" LEFT JOIN Person pe ON (pe.id = pa.personId) " +
			" LEFT JOIN PersonExtended petd ON (pe.id = petd.id) "+
			" LEFT JOIN DoctorsOffice dso ON (dso.id = ece.doctorsOfficeId) "+
			" LEFT JOIN PoliceInterventionDetails pi ON (pi.id = ece.id) "+
			" JOIN TriageCategory tc ON (tc.id = ece.triageCategoryId)" +
			" LEFT JOIN Shockroom s ON (s.id = ece.shockroomId)" +
			" LEFT JOIN Bed b ON (ece.bedId = b.id) " +
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
	@Query(value = " UPDATE EmergencyCareEpisode AS ece " +
			" SET ece.emergencyCareStateId = :emergencyCareStateId, " +
			" ece.shockroomId = :shockroomId, " +
			" ece.updateable.updatedOn = CURRENT_TIMESTAMP " +
			" WHERE ece.id = :episodeId "+
			" AND ece.institutionId = :institutionId ")
	void updateStateWithShockroom(@Param("episodeId") Integer episodeId,
					 @Param("institutionId") Integer institutionId,
					 @Param("emergencyCareStateId") Short emergencyCareStateId,
					 @Param("shockroomId") Integer shockroomId);

	@Transactional
	@Modifying
	@Query(value = " UPDATE EmergencyCareEpisode AS ece " +
			" SET ece.emergencyCareStateId = :emergencyCareStateId, " +
			" ece.bedId = :bedId, " +
			" ece.updateable.updatedOn = CURRENT_TIMESTAMP " +
			" WHERE ece.id = :episodeId "+
			" AND ece.institutionId = :institutionId ")
	void updateStateWithBed(@Param("episodeId") Integer episodeId,
							@Param("institutionId") Integer institutionId,
							@Param("emergencyCareStateId") Short emergencyCareStateId,
							@Param("bedId") Integer bedId);

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

	@Transactional(readOnly = true)
	@Query("SELECT ece.id " +
			"FROM EmergencyCareEpisode ece " +
			"WHERE ece.patientId IN :patientsIds")
	List<Integer> getEmergencyCareEpisodeIdsFromPatients(@Param("patientsIds")List<Integer> patientsIds);

	@Transactional(readOnly = true)
	@Query("SELECT orf " +
			"FROM Triage t " +
			"JOIN TriageRiskFactors trf ON (t.id = trf.pk.triageId) " +
			"JOIN ObservationRiskFactor orf ON (trf.pk.observationRiskFactorId = orf.id) " +
			"WHERE t.emergencyCareEpisodeId IN :eceIds")
	List<ObservationRiskFactor> getObservationRiskFactorFromEmergencyCareEpisodes(@Param("eceIds") List<Integer> eceIds);

	@Transactional(readOnly = true)
	@Query("SELECT ece " +
			"FROM EmergencyCareEpisode ece " +
			"WHERE ece.patientId IN :patientIds " +
			"AND ece.emergencyCareStateId IN :statusIds " +
			"AND (ece.deleteable.deleted = false or ece.deleteable.deleted is null)")
	List<EmergencyCareEpisode> getFromPatientsAndStatus(@Param("patientIds") List<Integer> patientIds, @Param("statusIds") List<Short> statusIds);
	
	@Transactional(readOnly = true)
	@Query( value = "SELECT new net.pladema.emergencycare.repository.domain.PatientECEVo(pa.id, pa.typeId) " +
			"FROM EmergencyCareEpisode ece " +
			"JOIN Patient pa ON (ece.patientId = pa.id) " +
			"WHERE ece.id = :episodeId ")
	PatientECEVo getPatientDataByEpisodeId(@Param("episodeId") Integer episodeId);

	@Transactional(readOnly = true)
	@Query("SELECT NEW net.pladema.emergencycare.repository.domain.ProfessionalPersonVo(p.firstName, " +
			"p.lastName, pe.nameSelfDetermination, p.middleNames, p.otherLastNames) " +
			"FROM EmergencyCareEpisode AS ece " +
			"JOIN UserPerson up ON (up.pk.userId = ece.updateable.updatedBy) " +
			"JOIN Person p ON (up.pk.personId = p.id) " +
			"JOIN PersonExtended pe ON (p.id = pe.id) " +
			"WHERE ece.id = :episodeId")
	ProfessionalPersonVo getEmergencyCareEpisodeRelatedProfessionalInfo(@Param("episodeId") Integer episodeId);
	
	@Transactional(readOnly = true)
	@Query(value = "SELECT ece.patientMedicalCoverageId " +
			"FROM EmergencyCareEpisode ece " +
			"WHERE ece.id = :episodeId")
	Integer getPatientMedicalCoverageIdByEpisodeId(@Param("episodeId") Integer episodeId);

	@Transactional(readOnly = true)
	@Query(value = "SELECT COUNT (ece.id) " +
			"FROM EmergencyCareEpisode as ece " +
			"WHERE (:doctorsOfficeId IS NULL OR ece.doctorsOfficeId = :doctorsOfficeId) " +
			"AND (:shockroomId IS NULL OR ece.shockroomId = :shockroomId) " +
			"AND ece.emergencyCareStateId = " + EmergencyCareState.EN_ATENCION)
	Integer existsEpisodeInOffice(@Param("doctorsOfficeId") Integer doctorsOfficeId,
								  @Param("shockroomId") Integer shockroomId);

	@Transactional(readOnly = true)
	@Query("SELECT COUNT(ece.id) > 0 FROM EmergencyCareEpisode ece WHERE ece.bedId = :bedId AND ece.emergencyCareStateId = " + EmergencyCareState.EN_ATENCION)
	boolean isBedOccupiedByEmergencyEpisode(@Param("bedId") Integer bedId);
}
