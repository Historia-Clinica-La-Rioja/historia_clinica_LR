package net.pladema.emergencycare.repository;

import ar.lamansys.sgh.clinichistory.infrastructure.output.repository.document.DocumentType;
import ar.lamansys.sgh.clinichistory.infrastructure.output.repository.ips.entity.ObservationRiskFactor;
import ar.lamansys.sgh.shared.domain.EmergencyCareEpisodeNotificationBo;
import ar.lamansys.sgx.shared.auditable.repository.SGXAuditableEntityJPARepository;
import net.pladema.emergencycare.repository.domain.EmergencyCareVo;
import net.pladema.emergencycare.repository.domain.PatientECEVo;
import net.pladema.emergencycare.repository.domain.ProfessionalPersonVo;
import net.pladema.emergencycare.repository.entity.EmergencyCareEpisode;
import net.pladema.emergencycare.repository.entity.EmergencyCareState;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;

@Repository
public interface EmergencyCareEpisodeRepository extends SGXAuditableEntityJPARepository<EmergencyCareEpisode, Integer> {

	@Transactional(readOnly = true)
	@Query(value = "SELECT ece " +
			" FROM EmergencyCareEpisode ece " +
			" WHERE ece.patientId = :patientId " +
			" AND ece.emergencyCareStateId IN (:states) " +
			" AND ece.institutionId = :institutionId " +
			" ORDER BY ece.id DESC")
	List<EmergencyCareEpisode> getEmergencyCareEpisodeInProgressByInstitution(@Param("institutionId") Integer institutionId,
																			  @Param("patientId") Integer patientId,
																			  @Param("states") List<Short> states);

	@Transactional(readOnly = true)
	@Query(value = "SELECT NEW net.pladema.emergencycare.repository.domain.EmergencyCareVo(ece.id, ece.institutionId) " +
			" FROM EmergencyCareEpisode ece " +
			" WHERE ece.patientId = :patientId  " +
			" AND ece.emergencyCareStateId IN (:states) ")
	List<EmergencyCareVo> getEmergencyCareEpisodeInProgress(@Param("patientId") Integer patientId,
															@Param("states") List<Short> states);

	@Transactional(readOnly = true)
	@Query(value = " SELECT NEW net.pladema.emergencycare.repository.domain.EmergencyCareVo(ece, pe, pa.typeId, petd.nameSelfDetermination, dso, tc, pi, s, b, ecd.administrativeDischargeOn, r, inst.name) "+
			" FROM EmergencyCareEpisode ece "+
			" JOIN Institution inst ON (inst.id = ece.institutionId) " +
			" LEFT JOIN EmergencyCareDischarge ecd ON (ecd.emergencyCareEpisodeId = ece.id) " +
			" LEFT JOIN Patient pa ON (pa.id = ece.patientId) "+
			" LEFT JOIN Person pe ON (pe.id = pa.personId) " +
			" LEFT JOIN PersonExtended petd ON (pe.id = petd.id) "+
			" LEFT JOIN DoctorsOffice dso ON (dso.id = ece.doctorsOfficeId) "+
			" LEFT JOIN PoliceInterventionDetails pi ON (pi.id = ece.id) "+
			" JOIN TriageCategory tc ON (tc.id = ece.triageCategoryId)" +
			" LEFT JOIN Shockroom s ON (s.id = ece.shockroomId)" +
			" LEFT JOIN Bed b ON (ece.bedId = b.id) " +
			" LEFT JOIN Room r ON b.roomId = r.id" +
			" WHERE ece.id = :episodeId ")
	Optional<EmergencyCareVo> getEpisode(@Param("episodeId") Integer episodeId);

	@Transactional(readOnly = true)
	@Query(value = " SELECT NEW net.pladema.emergencycare.repository.domain.EmergencyCareVo(ece, pe, pa.typeId, petd.nameSelfDetermination, dso, tc, pi, s, b, ecd.administrativeDischargeOn, r) "+
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
			" LEFT JOIN Room r ON b.roomId = r.id" +
			" WHERE ece.id = :episodeId " +
			" AND ece.institutionId = :institutionId ")
	Optional<EmergencyCareVo> getEpisodeByEpisodeAndInstitutionId(@Param("episodeId") Integer episodeId, @Param("institutionId") Integer institutionId);

	@Transactional(readOnly = true)
	@Query(value =" SELECT ece.emergencyCareStateId " +
			" FROM EmergencyCareEpisode ece "+
			" WHERE ece.id = :episodeId ")
	Optional<Short> getState(@Param("episodeId") Integer episodeId);

	@Transactional
	@Modifying
	@Query(value = " UPDATE EmergencyCareEpisode AS ece " +
			" SET ece.emergencyCareStateId = :emergencyCareStateId, " +
			" ece.doctorsOfficeId = :doctorsOfficeId, " +
			" ece.bedId = NULL, " +
			" ece.shockroomId = NULL, " +
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
			" ece.bedId = NULL, " +
			" ece.doctorsOfficeId = NULL, " +
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
			" ece.shockroomId = NULL, " +
			" ece.doctorsOfficeId = NULL, " +
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
	@Query(value = "SELECT (case when count(ece.id) > 0 then true else false end) " +
			"FROM EmergencyCareEpisode ece " +
			"WHERE ece.patientId = :patientId " +
			"AND ece.emergencyCareStateId IN (:states) " +
			"AND ece.institutionId = :institutionId")
	boolean existsActiveEpisodeByPatientIdAndInstitutionId(@Param("patientId") Integer patientId,
														   @Param("institutionId") Integer institutionId,
														   @Param("states") List<Short> states);

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
			"JOIN HistoricEmergencyEpisode hec ON (ece.id = hec.pk.emergencyCareEpisodeId) " +
			"JOIN UserPerson up ON (up.pk.userId = hec.creationable.createdBy) " +
			"JOIN Person p ON (up.pk.personId = p.id) " +
			"JOIN PersonExtended pe ON (p.id = pe.id) " +
			"WHERE ece.id = :episodeId " +
			"AND hec.creationable.createdOn = (" +
			"	SELECT MAX(hec2.creationable.createdOn) " +
			"	FROM HistoricEmergencyEpisode hec2 " +
			"	WHERE hec2.pk.emergencyCareEpisodeId = ece.id " +
			")")
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
	
	@Transactional(readOnly = true)
	@Query(value = "SELECT COUNT(*) > 0 " +
			"FROM EmergencyCareEpisode ece " +
			"JOIN Document d ON (ece.id = d.sourceId) " +
			"WHERE ece.id = :episodeId " +
			"AND d.typeId IN (" +
				DocumentType.EMERGENCY_CARE_EVOLUTION_NOTE + ", " + DocumentType.NURSING_EMERGENCY_CARE_EVOLUTION +
			")"
	)
	Boolean episodeHasEvolutionNote(@Param("episodeId") Integer episodeId);

	@Transactional(readOnly = true)
	@Query("SELECT bedId " +
			"FROM EmergencyCareEpisode ece " +
			"WHERE id = :emergencyCareEpisodeId ")
	Integer getEmergencyCareEpisodeBedId(@Param("emergencyCareEpisodeId") Integer emergencyCareEpisodeId);

	@Transactional(readOnly = true)
	@Query("SELECT shockroomId " +
			"FROM EmergencyCareEpisode ece " +
			"WHERE id = :emergencyCareEpisodeId ")
	Integer getEmergencyCareEpisodeShockroomId(@Param("emergencyCareEpisodeId") Integer emergencyCareEpisodeId);

	@Transactional(readOnly = true)
	@Query("SELECT doctorsOfficeId " +
			"FROM EmergencyCareEpisode ece " +
			"WHERE id = :emergencyCareEpisodeId ")
	Integer getEmergencyCareEpisodeDoctorsOfficeId(@Param("emergencyCareEpisodeId") Integer emergencyCareEpisodeId);

	@Transactional(readOnly = true)
	@Query( value = "SELECT  (case when count(ece.id)> 0 then true else false end) " +
			"FROM EmergencyCareEpisode ece " +
			"WHERE ece.patientId = :patientId " +
			"AND ece.emergencyCareStateId IN (:states) " )
	boolean existsActiveEpisodeByPatientId(@Param("patientId") Integer patientId,
										   @Param("states") List<Short> states);

	@Transactional(readOnly = true)
	@Query("SELECT ep.id " +
			"FROM EmergencyCareEpisode ep " +
			"WHERE ep.institutionId = :institutionId " +
			"AND ep.patientId = :patientId " +
			"AND ep.creationable.createdOn <= :date " +
			"ORDER BY ep.creationable.createdOn DESC")
	Page<Integer> getInternmentEpisodeIdByDate(@Param("institutionId") Integer institutionId,
											   @Param("patientId") Integer patientId,
											   @Param("date") LocalDateTime date,
											   Pageable pageable);

	@Transactional(readOnly = true)
	@Query(" SELECT b.roomId " +
			"FROM EmergencyCareEpisode ece " +
			"JOIN Bed b ON (b.id = ece.bedId) " +
			"WHERE ece.id = :emergencyCareEpisodeId")
	Integer getRoomId(@Param("emergencyCareEpisodeId") Integer emergencyCareEpisodeId);

	@Transactional(readOnly = true)
	@Query(" SELECT NEW ar.lamansys.sgh.shared.domain.EmergencyCareEpisodeNotificationBo(ece.id, p2.firstName, p2.lastName, CASE WHEN ece.bedId IS NOT NULL THEN CONCAT('Habitacion ', r.roomNumber) WHEN ece.shockroomId IS NOT NULL THEN sr.description ELSE do.description END, " +
			"tc.name, tc.colorCode, CASE WHEN ece.bedId IS NOT NULL THEN r.topic WHEN ece.shockroomId IS NOT NULL THEN sr.topic ELSE do.topic END) " +
			"FROM EmergencyCareEpisode ece " +
			"LEFT JOIN Patient p ON (p.id = ece.patientId)" +
			"LEFT JOIN Person p2 ON (p2.id = p.personId) " +
			"LEFT JOIN DoctorsOffice do ON (do.id = ece.doctorsOfficeId) " +
			"LEFT JOIN Bed b ON (b.id = ece.bedId) " +
			"LEFT JOIN Room r ON (r.id = b.roomId) " +
			"LEFT JOIN Shockroom sr ON (sr.id = ece.shockroomId) " +
			"JOIN TriageCategory tc ON (tc.id = ece.triageCategoryId) " +
			"WHERE ece.id = :episodeId")
    EmergencyCareEpisodeNotificationBo getSchedulerNotificationData(@Param("episodeId") Integer episodeId);

	@Transactional
	@Modifying
	@Query(value = "UPDATE EmergencyCareEpisode ece " +
					"SET ece.patientDescription = :patientDescription, " +
					"ece.updateable.updatedOn = CURRENT_TIMESTAMP " +
					"WHERE ece.id = :episodeId")
	void updatePatientDescription(@Param("episodeId") Integer episodeId,
					 			  @Param("patientDescription") String patientDescription);

	@Transactional(readOnly = true)
	@Query( value = "SELECT (case when count(ecd.emergencyCareEpisodeId) > 0 then true else false end) " +
			"FROM EmergencyCareDischarge ecd " +
			"WHERE ecd.emergencyCareEpisodeId = :episodeId")
	boolean isEpisodeMedicalOrAdministrativeDischarge(@Param("episodeId") Integer episodeId);

	@Transactional(readOnly = true)
	@Query("SELECT ece FROM EmergencyCareEpisode ece WHERE ece.bedId = :bedId AND ece.emergencyCareStateId = " + EmergencyCareState.EN_ATENCION)
	Optional<EmergencyCareEpisode> findByBedIdInAttention(@Param("bedId") Integer bedId);

	@Transactional(readOnly = true)
	@Query("SELECT ece FROM EmergencyCareEpisode ece WHERE ece.shockroomId = :shockroomId AND ece.emergencyCareStateId = " + EmergencyCareState.EN_ATENCION)
	Optional<EmergencyCareEpisode> findByShockroomIdInAttention(@Param("shockroomId") Integer shockroomId);

	@Transactional(readOnly = true)
	@Query("SELECT ece FROM EmergencyCareEpisode ece WHERE ece.doctorsOfficeId = :doctorsOfficeId AND ece.emergencyCareStateId = " + EmergencyCareState.EN_ATENCION)
	Optional<EmergencyCareEpisode> findByDoctorsOfficeIdInAttention(@Param("doctorsOfficeId") Integer doctorsOfficeId);
}
