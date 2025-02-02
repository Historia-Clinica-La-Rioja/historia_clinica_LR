package net.pladema.medicalconsultation.diary.repository;

import net.pladema.medicalconsultation.appointment.repository.entity.AppointmentState;
import net.pladema.medicalconsultation.diary.repository.domain.DiaryOpeningHoursVo;
import net.pladema.medicalconsultation.diary.repository.domain.OccupationVo;
import net.pladema.medicalconsultation.diary.repository.entity.DiaryOpeningHours;
import net.pladema.medicalconsultation.diary.repository.entity.DiaryOpeningHoursPK;
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
import java.util.Set;

@Repository
public interface DiaryOpeningHoursRepository extends JpaRepository<DiaryOpeningHours, DiaryOpeningHoursPK> {

    @Transactional(readOnly = true)
    @Query("SELECT new net.pladema.medicalconsultation.diary.repository.domain.OccupationVo( " +
            "d.id, d.startDate, d.endDate, oh.dayWeekId, oh.from, oh.to) " +
            "FROM DiaryOpeningHours AS doh " +
            "JOIN Diary AS d ON ( doh.pk.diaryId = d.id ) " +
            "JOIN OpeningHours AS oh ON ( doh.pk.openingHoursId = oh.id ) " +
            "WHERE d.doctorsOfficeId = :doctorsOfficeId " +
            "AND d.startDate <= :endDate " +
            "AND d.endDate >= :startDate " +
            "AND d.deleteable.deleted = false " +
            "ORDER BY oh.dayWeekId, oh.from")
    List<OccupationVo> findAllWeeklyDoctorsOfficeOccupation(@Param("doctorsOfficeId") Integer doctorsOfficeId,
                                                            @Param("startDate")LocalDate startDate,
                                                            @Param("endDate")LocalDate endDate);

    @Transactional(readOnly = true)
    @Query("SELECT NEW net.pladema.medicalconsultation.diary.repository.domain.DiaryOpeningHoursVo( " +
            "d.id, oh, doh.medicalAttentionTypeId, doh.overturnCount, doh.externalAppointmentsAllowed, " +
			"doh.protectedAppointmentsAllowed, doh.onSiteAttentionAllowed, doh.patientVirtualAttentionAllowed, " +
			"doh.secondOpinionVirtualAttentionAllowed, doh.regulationProtectedAppointmentsAllowed) " +
            "FROM DiaryOpeningHours AS doh " +
            "JOIN Diary AS d ON ( doh.pk.diaryId = d.id ) " +
            "JOIN OpeningHours AS oh ON ( doh.pk.openingHoursId = oh.id ) " +
            "WHERE doh.pk.diaryId IN (:diaryIds) " +
            "AND d.deleteable.deleted = false " +
            "ORDER BY oh.dayWeekId, oh.from")
    List<DiaryOpeningHoursVo> getDiariesOpeningHours(@Param("diaryIds") List<Integer> diaryIds);

	@Transactional(readOnly = true)
	@Query("SELECT NEW net.pladema.medicalconsultation.diary.repository.domain.DiaryOpeningHoursVo( " +
			"d.id, oh, doh.medicalAttentionTypeId, doh.overturnCount, doh.externalAppointmentsAllowed, " +
			"doh.protectedAppointmentsAllowed, doh.onSiteAttentionAllowed, doh.patientVirtualAttentionAllowed, " +
			"doh.secondOpinionVirtualAttentionAllowed, doh.regulationProtectedAppointmentsAllowed) " +
			"FROM DiaryOpeningHours AS doh " +
			"JOIN Diary AS d ON ( doh.pk.diaryId = d.id AND doh.medicalAttentionTypeId = :medicalAttentionTypeId) " +
			"JOIN OpeningHours AS oh ON ( doh.pk.openingHoursId = oh.id ) " +
			"WHERE doh.pk.diaryId IN (:diaryIds) " +
			"AND d.deleteable.deleted = false ")
	List<DiaryOpeningHoursVo> getDiariesOpeningHoursByMedicalAttentionType(@Param("diaryIds") List<Integer> diaryIds,
																		   @Param("medicalAttentionTypeId") Short medicalAttentionTypeId);

	@Transactional(readOnly = true)
	@Query("SELECT NEW net.pladema.medicalconsultation.diary.repository.domain.DiaryOpeningHoursVo( " +
			"d.id, oh, doh.medicalAttentionTypeId, doh.overturnCount, doh.externalAppointmentsAllowed, " +
			"doh.protectedAppointmentsAllowed, doh.onSiteAttentionAllowed, doh.patientVirtualAttentionAllowed, " +
			"doh.secondOpinionVirtualAttentionAllowed, doh.regulationProtectedAppointmentsAllowed) " +
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

	@Transactional(readOnly = true)
	@Query(" SELECT doh.patientVirtualAttentionAllowed " +
					"FROM DiaryOpeningHours doh " +
					"WHERE doh.pk.diaryId = :diaryId AND doh.pk.openingHoursId = :openingHoursId")
	Boolean isPatientVirtualConsultationAllowed(@Param("diaryId") Integer diaryId, @Param("openingHoursId") Integer openingHoursId);

	@Transactional(readOnly = true)
	@Query(" SELECT doh.secondOpinionVirtualAttentionAllowed " +
					"FROM DiaryOpeningHours doh " +
					"WHERE doh.pk.diaryId = :diaryId AND doh.pk.openingHoursId = :openingHoursId")
	Boolean isSecondOpinionVirtualConsultationAllowed(@Param("diaryId") Integer diaryId, @Param("openingHoursId") Integer openingHoursId);

	@Transactional(readOnly = true)
	@Query("SELECT doh.externalAppointmentsAllowed " +
			"FROM DiaryOpeningHours doh " +
			"WHERE doh.pk.diaryId = :diaryId " +
			"AND doh.pk.openingHoursId = :openingHoursId")
	Boolean getIfExternalAppointmentsAreAllowed(@Param("diaryId") Integer diaryId, @Param("openingHoursId") Integer openingHoursId);

	@Modifying
	@Query("DELETE FROM DiaryOpeningHours doh WHERE doh.pk.diaryId = :diaryId AND doh.pk NOT IN :newPKs")
	void deleteAllByDiaryIdAndNotInPK(@Param("diaryId") Integer diaryId, @Param("newPKs") Set<DiaryOpeningHoursPK> newPKs);
}
