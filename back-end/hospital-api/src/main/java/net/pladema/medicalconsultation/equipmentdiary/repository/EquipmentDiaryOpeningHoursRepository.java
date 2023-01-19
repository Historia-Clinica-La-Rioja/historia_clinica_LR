package net.pladema.medicalconsultation.equipmentdiary.repository;

import net.pladema.medicalconsultation.diary.repository.domain.DiaryOpeningHoursVo;
import net.pladema.medicalconsultation.diary.repository.domain.OccupationVo;

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
			"ed.id, oh, edoh.medicalAttentionTypeId, edoh.overturnCount, edoh.externalAppointmentsAllowed) " +
			"FROM EquipmentDiaryOpeningHours AS edoh " +
			"JOIN EquipmentDiary AS ed ON ( edoh.pk.equipmentDiaryId = ed.id ) " +
			"JOIN OpeningHours AS oh ON ( edoh.pk.openingHoursId = oh.id ) " +
			"WHERE edoh.pk.equipmentDiaryId = :equipmentDiaryId " +
			"AND ed.deleteable.deleted = false " +
			"ORDER BY oh.dayWeekId, oh.from")
	List<DiaryOpeningHoursVo> getDiaryOpeningHours(@Param("equipmentDiaryId") Integer diaryId);



	@Transactional
	@Modifying
	@Query("DELETE FROM EquipmentDiaryOpeningHours doh WHERE doh.pk.equipmentDiaryId = :equipmentDiaryId ")
	void deleteAll(@Param("equipmentDiaryId") Integer equipmentDiaryId);





	@Transactional(readOnly = true)
	@Query("SELECT new net.pladema.medicalconsultation.diary.repository.domain.OccupationVo( " +
			"ed.id, ed.startDate, ed.endDate, oh.dayWeekId, oh.from, oh.to) " +
			"FROM EquipmentDiaryOpeningHours AS edoh " +
			"JOIN EquipmentDiary AS ed ON ( edoh.pk.equipmentDiaryId = ed.id ) " +
			"JOIN OpeningHours AS oh ON ( edoh.pk.openingHoursId = oh.id ) " +
			"WHERE ed.equipmentId = :equipmentId " +
			"AND ed.startDate <= :endDate " +
			"AND ed.endDate >= :startDate " +
			"AND ed.deleteable.deleted = false " +
			"ORDER BY oh.dayWeekId, oh.from")
	List<OccupationVo> findAllWeeklyEquipmentOccupation(@Param("equipmentId") Integer equipmentId,
															@Param("startDate")LocalDate startDate,
															@Param("endDate")LocalDate endDate);


	@Transactional(readOnly = true)
	@Query( "SELECT (case when count(edoh) > 0 then true else false end) " +
			"FROM EquipmentDiaryOpeningHours AS edoh " +
			"JOIN EquipmentDiary AS ed ON (ed.id = edoh.pk.equipmentDiaryId) " +
			"JOIN OpeningHours  AS oh ON (edoh.pk.openingHoursId = oh.id) " +
			"WHERE ed.id = :equipmentDiaryId " +
			"AND oh.dayWeekId = :dayWeekId " +
			"AND ed.equipmentId = :equipmentId " +
			"AND ((oh.from < :to) AND (oh.to > :from) )" )
	boolean overlapDiaryOpeningHoursFromOtherDiary(@NotNull @Param("equipmentDiaryId") Integer equipmentDiaryId,
												   @NotNull @Param("equipmentId") Integer equipmentId,
												   @NotNull @Param("dayWeekId") Short dayWeekId,
												   @NotNull @Param("from") LocalTime from, @Param("to") LocalTime to);
}
