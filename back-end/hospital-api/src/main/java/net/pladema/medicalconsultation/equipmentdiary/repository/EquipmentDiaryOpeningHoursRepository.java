package net.pladema.medicalconsultation.equipmentdiary.repository;

import net.pladema.medicalconsultation.appointment.repository.entity.AppointmentState;
import net.pladema.medicalconsultation.diary.repository.domain.DiaryOpeningHoursVo;
import net.pladema.medicalconsultation.diary.repository.domain.OccupationVo;
import net.pladema.medicalconsultation.diary.repository.entity.DiaryOpeningHours;
import net.pladema.medicalconsultation.diary.repository.entity.DiaryOpeningHoursPK;

import net.pladema.medicalconsultation.equipmentdiary.repository.entity.EquipmentDiaryOpeningHours;

import net.pladema.medicalconsultation.equipmentdiary.repository.entity.EquipmentDiaryOpeningHoursPK;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;

import javax.validation.constraints.NotNull;

import java.time.LocalDate;
import java.time.LocalTime;
import java.util.List;

@Repository
public interface EquipmentDiaryOpeningHoursRepository extends JpaRepository<EquipmentDiaryOpeningHours, EquipmentDiaryOpeningHoursPK> {



    @Transactional(readOnly = true)
    @Query("SELECT NEW net.pladema.medicalconsultation.diary.repository.domain.DiaryOpeningHoursVo( " +
            "d.id, oh, doh.medicalAttentionTypeId, doh.overturnCount, doh.externalAppointmentsAllowed) " +
            "FROM DiaryOpeningHours AS doh " +
            "JOIN Diary AS d ON ( doh.pk.diaryId = d.id ) " +
            "JOIN OpeningHours AS oh ON ( doh.pk.openingHoursId = oh.id ) " +
            "WHERE doh.pk.diaryId IN (:diaryIds) " +
            "AND d.deleteable.deleted = false " +
            "ORDER BY oh.dayWeekId, oh.from")
    List<DiaryOpeningHoursVo> getDiariesOpeningHours(@Param("diaryIds") List<Integer> diaryIds);

	@Transactional(readOnly = true)
	@Query("SELECT NEW net.pladema.medicalconsultation.diary.repository.domain.DiaryOpeningHoursVo( " +
			"d.id, oh, doh.medicalAttentionTypeId, doh.overturnCount, doh.externalAppointmentsAllowed) " +
			"FROM DiaryOpeningHours AS doh " +
			"JOIN Diary AS d ON ( doh.pk.diaryId = d.id ) " +
			"JOIN OpeningHours AS oh ON ( doh.pk.openingHoursId = oh.id ) " +
			"WHERE doh.pk.diaryId = :diaryId " +
			"AND d.deleteable.deleted = false " +
			"ORDER BY oh.dayWeekId, oh.from")
	List<DiaryOpeningHoursVo> getDiaryOpeningHours(@Param("diaryId") Integer diaryId);


	@Transactional(readOnly = true)
    @Query( "SELECT (case when count(doh) > 0 then true else false end) " +
            "FROM DiaryOpeningHours AS doh " +
            "WHERE doh.pk.diaryId = :diaryId " +
            "AND doh.pk.openingHoursId = :openingHoursId " +
            "AND doh.overturnCount > (SELECT COUNT(a.id) " +
            "                           FROM Appointment AS a " +
            "                           JOIN AppointmentAssn AS aa ON (a.id = aa.pk.appointmentId) " +
            "                           WHERE aa.pk.diaryId = :diaryId " +
            "                           AND aa.pk.openingHoursId = :openingHoursId " +
            "							AND a.dateTypeId = :newAppointmentDate"+
            "                           AND a.isOverturn = true  " +
            "                           AND NOT a.appointmentStateId = " + AppointmentState.CANCELLED_STR + ")" )
	boolean allowNewOverturn(@NotNull @Param("diaryId") Integer diaryId,
                             @NotNull @Param("openingHoursId") Integer openingHoursId,
                             @NotNull @Param("newAppointmentDate") LocalDate newAppointmentDate);

	@Transactional
	@Modifying
	@Query("DELETE FROM DiaryOpeningHours doh WHERE doh.pk.diaryId = :diaryId ")
	void deleteAll(@Param("diaryId") Integer diaryId);



    @Transactional(readOnly = true)
    @Query( "SELECT (case when count(doh) > 0 then true else false end) " +
            "FROM DiaryOpeningHours AS doh " +
            "JOIN Diary AS d ON (d.id = doh.pk.diaryId) " +
            "JOIN OpeningHours  AS oh ON (doh.pk.openingHoursId = oh.id) " +
            "WHERE d.id = :diaryId " +
            "AND oh.dayWeekId = :dayWeekId " +
            "AND d.doctorsOfficeId = :doctorsOfficeId " +
            "AND ((oh.from < :to) AND (oh.to > :from) )" )
    boolean overlapDiaryOpeningHoursFromOtherDiary(@NotNull @Param("diaryId") Integer diaryId,
                                                   @NotNull @Param("doctorsOfficeId") Integer doctorsOfficeId,
                                                   @NotNull @Param("dayWeekId") Short dayWeekId,
                                                   @NotNull @Param("from") LocalTime from, @Param("to") LocalTime to);
}
