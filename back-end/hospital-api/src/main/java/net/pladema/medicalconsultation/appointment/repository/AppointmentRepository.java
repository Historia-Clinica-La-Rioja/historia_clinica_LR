package net.pladema.medicalconsultation.appointment.repository;

import net.pladema.medicalconsultation.appointment.repository.domain.AppointmentDiaryVo;
import net.pladema.medicalconsultation.appointment.repository.entity.Appointment;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDate;
import java.time.LocalTime;
import java.util.List;

@Repository
public interface AppointmentRepository extends JpaRepository<Appointment, Integer> {

    @Transactional(readOnly = true)
    @Query( "SELECT NEW net.pladema.medicalconsultation.appointment.repository.domain.AppointmentDiaryVo(aa.pk.diaryId, a)" +
            "FROM Appointment AS a " +
            "JOIN AppointmentAssn AS aa ON (a.id = aa.pk.appointmentId) " +
            "WHERE aa.pk.diaryId IN (:diaryIds) ")
    List<AppointmentDiaryVo> getAppointmentsByDiaries(@Param("diaryIds") List<Integer> diaryIds);


    @Transactional(readOnly = true)
    @Query( "SELECT (case when count(a.id)> 0 then true else false end) " +
            "FROM Appointment AS a " +
            "JOIN AppointmentAssn AS aa ON (a.id = aa.pk.appointmentId) " +
            "WHERE aa.pk.diaryId = :diaryId " +
            "AND aa.pk.openingHoursId = :openingHoursId " +
            "AND a.dateTypeId = :date " +
            "AND a.hour = :hour")
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
}
