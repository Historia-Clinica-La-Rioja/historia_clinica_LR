package net.pladema.medicalconsultation.appointment.repository;

import ar.lamansys.sgx.shared.auditable.repository.SGXAuditableEntityJPARepository;
import net.pladema.medicalconsultation.appointment.repository.domain.AppointmentAssignedForPatientVo;
import net.pladema.medicalconsultation.appointment.repository.domain.AppointmentDiaryVo;
import net.pladema.medicalconsultation.appointment.repository.domain.AppointmentVo;
import net.pladema.medicalconsultation.appointment.repository.domain.NotifyPatientVo;
import net.pladema.medicalconsultation.appointment.repository.entity.Appointment;
import net.pladema.medicalconsultation.appointment.repository.entity.AppointmentState;

import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDate;
import java.time.LocalTime;
import java.util.List;
import java.util.Optional;

@Repository
public interface AppointmentRepository extends SGXAuditableEntityJPARepository<Appointment, Integer> {

    @Transactional(readOnly = true)
    @Query( "SELECT NEW net.pladema.medicalconsultation.appointment.repository.domain.AppointmentDiaryVo(" +
            "aa.pk.diaryId, a.id, a.patientId, a.dateTypeId, a.hour, a.appointmentStateId, a.isOverturn, " +
            "a.patientMedicalCoverageId,a.phonePrefix, a.phoneNumber, doh.medicalAttentionTypeId)" +
            "FROM Appointment AS a " +
            "JOIN AppointmentAssn AS aa ON (a.id = aa.pk.appointmentId) " +
            "JOIN Diary d ON (d.id = aa.pk.diaryId )" +
            "JOIN DiaryOpeningHours  AS doh ON (doh.pk.diaryId = d.id) " +
            "WHERE aa.pk.diaryId IN (:diaryIds) AND (d.deleteable.deleted = false OR d.deleteable.deleted is null )" +
            "AND NOT a.appointmentStateId = " + AppointmentState.CANCELLED_STR +
			"AND a.deleteable.deleted = FALSE OR a.deleteable.deleted IS NULL " +
            "ORDER BY d.id,a.isOverturn")
    List<AppointmentDiaryVo> getAppointmentsByDiaries(@Param("diaryIds") List<Integer> diaryIds);
    
    @Transactional(readOnly = true)
    @Query( "SELECT NEW net.pladema.medicalconsultation.appointment.repository.domain.AppointmentVo(aa.pk.diaryId, a, doh.medicalAttentionTypeId, has.reason, ao.observation, ao.createdBy)" +
            "FROM Appointment AS a " +
            "JOIN AppointmentAssn AS aa ON (a.id = aa.pk.appointmentId) " +
			"LEFT JOIN AppointmentObservation AS ao ON (a.id = ao.appointmentId) " +
            "LEFT JOIN HistoricAppointmentState AS has ON (a.id = has.pk.appointmentId) " +
            "JOIN Diary d ON (d.id = aa.pk.diaryId )" +
			"JOIN DiaryOpeningHours  AS doh ON (doh.pk.diaryId = d.id) " +
            "WHERE a.id = :appointmentId " +
			"AND a.deleteable.deleted = FALSE OR a.deleteable.deleted IS NULL " +
            "AND ( has.pk.changedStateDate IS NULL OR has.pk.changedStateDate = " +
            "   ( SELECT MAX (subHas.pk.changedStateDate) FROM HistoricAppointmentState subHas WHERE subHas.pk.appointmentId = a.id) ) ")
    List<AppointmentVo> getAppointment(@Param("appointmentId") Integer appointmentId);

    @Transactional(readOnly = true)
    @Query( "SELECT NEW net.pladema.medicalconsultation.appointment.repository.domain.AppointmentDiaryVo(" +
            "aa.pk.diaryId, a.id, a.patientId, a.dateTypeId, a.hour, a.appointmentStateId, a.isOverturn, " +
            "a.patientMedicalCoverageId,a.phonePrefix, a.phoneNumber, doh.medicalAttentionTypeId) " +
            "FROM Appointment AS a " +
            "JOIN AppointmentAssn AS aa ON (a.id = aa.pk.appointmentId) " +
            "JOIN DiaryOpeningHours  AS doh ON (doh.pk.diaryId = aa.pk.diaryId) " +
            "WHERE aa.pk.diaryId = :diaryId AND a.appointmentStateId <> " + AppointmentState.CANCELLED_STR +
			"AND a.deleteable.deleted = FALSE OR a.deleteable.deleted IS NULL " +
            " AND a.dateTypeId >= CURRENT_DATE ")
    List<AppointmentDiaryVo> getFutureActiveAppointmentsByDiary(@Param("diaryId") Integer diaryId);

    @Transactional(readOnly = true)
    @Query( "SELECT (case when count(a.id)> 0 then true else false end) " +
            "FROM Appointment AS a " +
            "JOIN AppointmentAssn AS aa ON (a.id = aa.pk.appointmentId) " +
            "WHERE aa.pk.diaryId = :diaryId " +
            "AND aa.pk.openingHoursId = :openingHoursId " +
            "AND a.dateTypeId = :date " +
            "AND a.hour = :hour " +
            "AND NOT a.appointmentStateId = " + AppointmentState.CANCELLED_STR)
    boolean existAppointment(@Param("diaryId") Integer diaryId, @Param("openingHoursId") Integer openingHoursId,
                             @Param("date") LocalDate date, @Param("hour") LocalTime hour);

    @Transactional
    @Modifying
    @Query( "UPDATE Appointment  AS a " +
            "SET a.appointmentStateId = :appointmentStateId, " +
            "a.updateable.updatedOn = CURRENT_TIMESTAMP, " +
            "a.updateable.updatedBy = :userId " +
            "WHERE a.id = :appointmentId ")
    void updateState(@Param("appointmentId") Integer appointmentId,
                     @Param("appointmentStateId") short appointmentStateId,
                     @Param("userId") Integer userId);

    @Transactional(readOnly = true)
    @Query( "SELECT DISTINCT a.id, a.hour " +
            "FROM Appointment AS a " +
            "JOIN AppointmentAssn AS aa ON (a.id = aa.pk.appointmentId) " +
            "JOIN Diary AS d ON (d.id = aa.pk.diaryId) " +
            "WHERE a.patientId = :patientId " +
            "AND d.healthcareProfessionalId = :healthProfessionalId " +
            "AND a.dateTypeId = :appointmentDate " +
            "AND a.appointmentStateId = " + AppointmentState.CONFIRMED + " " +
            "ORDER BY a.hour ASC")
    List<Integer> getAppointmentsId(@Param("patientId") Integer patientId,
                                    @Param("healthProfessionalId")  Integer healthProfessionalId,
                                    @Param("appointmentDate")  LocalDate appointmentDate);


    @Transactional
    @Modifying
    @Query( "UPDATE Appointment  AS a " +
            "SET a.phoneNumber = :phoneNumber, " +
			"a.phonePrefix = :phonePrefix, " +
            "a.updateable.updatedOn = CURRENT_TIMESTAMP, " +
            "a.updateable.updatedBy = :userId " +
            "WHERE a.id = :appointmentId ")
    void updatePhoneNumber(@Param("appointmentId") Integer appointmentId,
						   @Param("phonePrefix") String phonePrefix,
                           @Param("phoneNumber") String phoneNumber,
                           @Param("userId") Integer userId);

	@Transactional
	@Modifying
	@Query( "UPDATE AppointmentObservation  AS ao " +
			"SET ao.observation = :observation, " +
			"ao.createdBy = :observationBy " +
			"WHERE ao.appointmentId = :appointmentId ")
	void updateObservation(@Param("appointmentId") Integer appointmentId,
						   @Param("observation") String observation,
						   @Param("observationBy") Integer observationBy);

    @Transactional(readOnly = true)
    @Query(name = "Appointment.medicalCoverage")
    List<Integer> getMedicalCoverage(@Param("patientId") Integer patientId,
                                     @Param("currentDate") LocalDate currentDate,
                                     @Param("appointmentState") Short appointmentState,
                                     @Param("professionalId") Integer professionalId);

	@Transactional(readOnly = true)
	@Query("SELECT a.patientMedicalCoverageId " +
			"FROM Appointment AS a " +
			"WHERE a.id = :appointmentId ")
	Optional<Integer> getAppointmentMedicalCoverageId(@Param("appointmentId") Integer appointmentId);

    @Transactional(readOnly = true)
    @Query( "SELECT NEW net.pladema.medicalconsultation.appointment.repository.domain.NotifyPatientVo(a.id, pp.lastName, pp.firstName, do.sectorId, php.lastName,php.firstName, do.description, do.topic)" +
            "FROM Appointment AS a " +
            "JOIN Patient AS p ON (p.id = a.patientId) " +
            "JOIN Person AS pp ON (pp.id = p.personId) " +
            "JOIN AppointmentAssn AS aa ON (a.id = aa.pk.appointmentId) " +
            "JOIN Diary d ON (d.id = aa.pk.diaryId) " +
            "JOIN DoctorsOffice AS do ON (do.id = d.doctorsOfficeId) " +
            "JOIN HealthcareProfessional AS hp ON (d.healthcareProfessionalId = hp.id)" +
            "JOIN Person AS php ON (php.id = hp.personId)" +
            "WHERE a.id = :appointmentId ")
    Optional<NotifyPatientVo> getNotificationData(@Param("appointmentId") Integer appointmentId);

	@Transactional(readOnly = true)
	@Query( "SELECT NEW net.pladema.medicalconsultation.appointment.repository.domain.AppointmentAssignedForPatientVo(" +
			"hp.licenseNumber, up.pk.userId, a.dateTypeId, a.hour, do.description)" +
			"FROM Appointment AS a " +
			"JOIN AppointmentAssn AS aa ON (a.id = aa.pk.appointmentId) " +
			"JOIN Diary d ON (d.id = aa.pk.diaryId )" +
			"JOIN HealthcareProfessional  AS hp ON (hp.id = d.healthcareProfessionalId) " +
			"JOIN UserPerson AS up ON (up.pk.personId = hp.personId )" +
			"JOIN DoctorsOffice do ON (do.id = d.doctorsOfficeId )" +
			"WHERE a.patientId = :patientId AND (d.deleteable.deleted = false OR d.deleteable.deleted is null )" +
			"AND a.appointmentStateId = " + AppointmentState.ASSIGNED)
	List<AppointmentAssignedForPatientVo> getAssignedAppointmentsByPatient(@Param("patientId") Integer patientId);

	@Transactional(readOnly = true)
	@Query( "SELECT a.id " +
			"FROM Appointment AS a " +
			"JOIN AppointmentAssn AS aa ON (a.id = aa.pk.appointmentId) " +
			"JOIN Diary AS d ON (d.id = aa.pk.diaryId )" +
			"JOIN DoctorsOffice AS do ON (do.id = d.doctorsOfficeId ) " +
			"JOIN HealthcareProfessional AS hcp ON(d.healthcareProfessionalId = hcp.id) " +
			"JOIN UserPerson AS up ON(hcp.personId = up.pk.personId) " +
			"WHERE a.patientId = :patientId " +
			"AND do.institutionId = :institutionId " +
			"AND up.pk.userId = :userId " +
			"AND a.appointmentStateId = " + AppointmentState.CONFIRMED + " " +
			"AND a.dateTypeId = :currentDate " +
			"AND a.hour >= :currentTime " +
			"ORDER BY a.dateTypeId, a.hour ASC ")
	List<Integer> getConfirmedAppointmentsByPatient(@Param("patientId") Integer patientId,
													@Param("institutionId") Integer institutionId,
													@Param("userId") Integer userId,
													@Param("currentDate") LocalDate currentDate,
													@Param("currentTime") LocalTime currentTime);

}
