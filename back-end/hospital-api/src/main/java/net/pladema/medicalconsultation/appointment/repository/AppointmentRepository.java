package net.pladema.medicalconsultation.appointment.repository;

import net.pladema.medicalconsultation.appointment.repository.domain.AppointmentDiaryVo;
import net.pladema.medicalconsultation.appointment.repository.domain.AppointmentVo;
import net.pladema.medicalconsultation.appointment.repository.entity.Appointment;
import net.pladema.medicalconsultation.appointment.repository.entity.AppointmentState;
import org.springframework.data.jpa.repository.JpaRepository;
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
public interface AppointmentRepository extends JpaRepository<Appointment, Integer> {

    @Transactional(readOnly = true)
    @Query( "SELECT NEW net.pladema.medicalconsultation.appointment.repository.domain.AppointmentDiaryVo(" +
            "aa.pk.diaryId, a.id, a.patientId, a.dateTypeId, a.hour, a.appointmentStateId, a.isOverturn, " +
            "a.patientMedicalCoverageId, a.phoneNumber, doh.medicalAttentionTypeId)" +
            "FROM Appointment AS a " +
            "JOIN AppointmentAssn AS aa ON (a.id = aa.pk.appointmentId) " +
            "JOIN Diary d ON (d.id = aa.pk.diaryId )" +
            "JOIN DiaryOpeningHours  AS doh ON (doh.pk.diaryId = d.id AND doh.pk.openingHoursId = aa.pk.openingHoursId) " +
            "WHERE aa.pk.diaryId IN (:diaryIds) AND d.deleteable.deleted = false " +
            "AND NOT a.appointmentStateId = " + AppointmentState.CANCELLED_STR +
            "ORDER BY d.id,a.isOverturn")
    List<AppointmentDiaryVo> getAppointmentsByDiaries(@Param("diaryIds") List<Integer> diaryIds);
    
    @Transactional(readOnly = true)
    @Query( "SELECT NEW net.pladema.medicalconsultation.appointment.repository.domain.AppointmentVo(aa.pk.diaryId, a, doh.medicalAttentionTypeId, has.reason )" +
            "FROM Appointment AS a " +
            "JOIN AppointmentAssn AS aa ON (a.id = aa.pk.appointmentId) " +
            "LEFT JOIN HistoricAppointmentState AS has ON (a.id = has.pk.appointmentId) " +
            "JOIN Diary d ON (d.id = aa.pk.diaryId )" +
            "JOIN DiaryOpeningHours  AS doh ON (doh.pk.diaryId = d.id AND doh.pk.openingHoursId = aa.pk.openingHoursId) " +
            "WHERE a.id = :appointmentId " +
            "AND ( has.pk.changedStateDate IS NULL OR has.pk.changedStateDate = " +
            "   ( SELECT MAX (subHas.pk.changedStateDate) FROM HistoricAppointmentState subHas WHERE subHas.pk.appointmentId = a.id) ) ")
    Optional<AppointmentVo> getAppointment(@Param("appointmentId") Integer appointmentId);

    @Transactional(readOnly = true)
    @Query( "SELECT NEW net.pladema.medicalconsultation.appointment.repository.domain.AppointmentDiaryVo(" +
            "aa.pk.diaryId, a.id, a.patientId, a.dateTypeId, a.hour, a.appointmentStateId, a.isOverturn, " +
            "a.patientMedicalCoverageId, a.phoneNumber, doh.medicalAttentionTypeId) " +
            "FROM Appointment AS a " +
            "JOIN AppointmentAssn AS aa ON (a.id = aa.pk.appointmentId) " +
            "JOIN DiaryOpeningHours  AS doh ON (doh.pk.diaryId = aa.pk.diaryId AND doh.pk.openingHoursId = aa.pk.openingHoursId) " +
            "WHERE aa.pk.diaryId = :diaryId AND a.appointmentStateId <> " + AppointmentState.CANCELLED_STR +
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
            "a.updateable.updatedOn = CURRENT_TIMESTAMP, " +
            "a.updateable.updatedBy = :userId " +
            "WHERE a.id = :appointmentId ")
    void updatePhoneNumber(@Param("appointmentId") Integer appointmentId,
                           @Param("phoneNumber") String phoneNumber,
                           @Param("userId") Integer userId);
}
