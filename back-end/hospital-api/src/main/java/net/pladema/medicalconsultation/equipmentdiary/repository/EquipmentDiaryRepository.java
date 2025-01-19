package net.pladema.medicalconsultation.equipmentdiary.repository;

import ar.lamansys.sgx.shared.auditable.repository.SGXAuditableEntityJPARepository;
import net.pladema.medicalconsultation.equipmentdiary.domain.UpdateEquipmentDiaryAppointmentBo;
import net.pladema.medicalconsultation.equipmentdiary.repository.domain.CompleteEquipmentDiaryListVo;
import net.pladema.medicalconsultation.equipmentdiary.repository.entity.EquipmentDiary;

import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDate;
import java.util.List;
import java.util.Optional;

public interface EquipmentDiaryRepository extends SGXAuditableEntityJPARepository<EquipmentDiary, Integer> {

	@Transactional(readOnly = true)
	@Query("SELECT id " +
			"FROM EquipmentDiary " +
			"WHERE equipmentId = :eqId " +
			"AND startDate <= :endDate " +
			"AND endDate >= :startDate " +
			"AND appointmentDuration = :appointmentDuration " +
			"AND deleteable.deleted = false")
	List<Integer> findAllOverlappingDiaryByEquipment(@Param("eqId") Integer doctorsOfficeId,
														@Param("startDate") LocalDate newDiaryStart,
														@Param("endDate") LocalDate newDiaryEnd,
														@Param("appointmentDuration") Short appointmentDuration);

	@Transactional(readOnly = true)
	@Query("SELECT id " +
			"FROM EquipmentDiary " +
			"WHERE equipmentId = :eqId " +
			"AND startDate <= :endDate " +
			"AND endDate >= :startDate " +
			"AND appointmentDuration = :appointmentDuration " +
			"AND id <> :excludeDiaryId " +
			"AND deleteable.deleted = false")
	List<Integer> findAllOverlappingDiaryByEquipmentExcludingDiary(
																	  @Param("eqId") Integer equipmentId,
																	  @Param("startDate") LocalDate newDiaryStart,
																	  @Param("endDate") LocalDate newDiaryEnd,
																	  @Param("appointmentDuration") Short appointmentDuration,
																	  @Param("excludeDiaryId") Integer excludeDiaryId);
	@Transactional(readOnly = true)
	@Query("SELECT ed " +
			"FROM EquipmentDiary AS ed " +
			"WHERE ed.equipmentId = :eqId " +
			"AND ed.startDate <= :endDate " +
			"AND ed.endDate >= :startDate " +
			"AND ed.deleteable.deleted = false")
	List<EquipmentDiary> findAllOverlappingDiary(@Param("eqId") Integer equipmentId,
										@Param("startDate") LocalDate newDiaryStart,
										@Param("endDate") LocalDate newDiaryEnd);

	@Transactional(readOnly = true)
	@Query("SELECT ed " +
			"FROM EquipmentDiary AS ed " +
			"WHERE ed.equipmentId = :eqId " +
			"AND ed.startDate <= :endDate " +
			"AND ed.endDate >= :startDate " +
			"AND ed.id <> :excludeDiaryId " +
			"AND ed.deleteable.deleted = false")
	List<EquipmentDiary> findAllOverlappingDiaryExcludingDiary(@Param("eqId") Integer equipmentId,
													  @Param("startDate") LocalDate newDiaryStart,
													  @Param("endDate") LocalDate newDiaryEnd,
													  @Param("excludeDiaryId") Integer excludeDiaryId);


	@Transactional(readOnly = true)
	@Query("SELECT ed " +
			"FROM EquipmentDiary ed " +
			"WHERE ed.equipmentId = :equipmentId " +
			"AND ed.active =:active "+
			"AND ed.deleteable.deleted = false")
	List<EquipmentDiary> getEquipmentDiariesFromEquipment(@Param("equipmentId") Integer equipmentId,
																 @Param("active") Boolean active);

	@Transactional(readOnly = true)
	@Query("SELECT NEW net.pladema.medicalconsultation.equipmentdiary.repository.domain.CompleteEquipmentDiaryListVo( " +
			"ed, s.id, s.description) " +
			"FROM EquipmentDiary ed " +
			"JOIN Equipment e ON e.id = ed.equipmentId " +
			"JOIN Sector s ON s.id = e.sectorId " +
			"WHERE ed.id = :equipmentDiaryId ")
	Optional<CompleteEquipmentDiaryListVo> getEquipmentDiary(@Param("equipmentDiaryId") Integer equipmentDiaryId);

	@Transactional(readOnly = true)
	@Query("SELECT d " +
			"FROM EquipmentDiary d " +
			"JOIN EquipmentAppointmentAssn aa ON aa.pk.equipmentDiaryId = d.id " +
			"WHERE aa.pk.appointmentId = :appointmentId ")
	Optional<EquipmentDiary> getDiaryByAppointment(@Param("appointmentId") Integer appointmentId);

	@Transactional(readOnly = true)
	@Query("SELECT NEW net.pladema.medicalconsultation.equipmentdiary.domain.UpdateEquipmentDiaryAppointmentBo(a.id, a.dateTypeId, a.hour, " +
			"a.appointmentStateId, edoh.medicalAttentionTypeId) " +
			"FROM EquipmentDiary ed " +
			"JOIN EquipmentAppointmentAssn eaa ON (eaa.pk.equipmentDiaryId = ed.id) " +
			"JOIN Appointment a ON (a.id = eaa.pk.appointmentId) " +
			"JOIN EquipmentDiaryOpeningHours edoh ON (edoh.pk.equipmentDiaryId = ed.id AND edoh.pk.openingHoursId = eaa.pk.openingHoursId) " +
			"WHERE ed.id = :equipmentDiaryId " +
			"AND a.appointmentStateId != :appointmentStateId " +
			"AND a.deleteable.deleted = FALSE " +
			"AND ed.deleteable.deleted = FALSE " +
			"AND a.dateTypeId >= CURRENT_DATE")
	List<UpdateEquipmentDiaryAppointmentBo> fetchUpdateEquipmentDiaryAppointments(@Param("equipmentDiaryId") Integer equipmentDiaryId,
																				  @Param("appointmentStateId") Short appointmentStateId);
}
