package net.pladema.medicalconsultation.equipmentdiary.service;

import net.pladema.medicalconsultation.equipmentdiary.service.domain.EquipmentDiaryBo;

import java.time.LocalDate;
import java.util.List;
import java.util.Optional;

public interface EquipmetDiaryService {
	Integer addDiary(EquipmentDiaryBo equipmentDiaryBo);

	List<Integer> getAllOverlappingEquipmentDiaryByEquipment(Integer equipmentId,LocalDate newDiaryStart,
															 LocalDate newDiaryEnd, Short appointmentDuration, Optional<Integer> excludeDiaryId);

}
