package net.pladema.medicalconsultation.equipmentdiary.repository;

import ar.lamansys.sgx.shared.auditable.repository.SGXAuditableEntityJPARepository;
import net.pladema.medicalconsultation.equipmentdiary.repository.entity.EquipmentDiary;

import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDate;
import java.util.List;

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




}
