package net.pladema.medicalconsultation.equipmentdiary.service;

import net.pladema.medicalconsultation.equipmentdiary.service.domain.CompleteEquipmentDiaryBo;
import net.pladema.medicalconsultation.equipmentdiary.service.domain.EquipmentDiaryBo;

import java.time.LocalDate;
import java.util.List;
import java.util.Optional;

public interface EquipmentDiaryService {
    Integer addDiary(EquipmentDiaryBo equipmentDiaryBo);

    List<Integer> getAllOverlappingEquipmentDiaryByEquipment(Integer equipmentId, LocalDate newDiaryStart,
                                                             LocalDate newDiaryEnd, Short appointmentDuration, Optional<Integer> excludeDiaryId);

    List<EquipmentDiaryBo> getAllOverlappingDiary(Integer doctorsOfficeId,
                                                  LocalDate newDiaryStart, LocalDate newDiaryEnd, Optional<Integer> excludeDiaryId);

    List<EquipmentDiaryBo> getEquipmentDiariesFromEquipment(Integer equipmentId,
                                                            Boolean active);

    Optional<CompleteEquipmentDiaryBo> getEquipmentDiary(Integer equipmentDiaryId);

    Integer updateDiary(EquipmentDiaryBo equipmentDiaryBo);

    Optional<EquipmentDiaryBo> getEquipmentDiaryByAppointment(Integer appointmentId);
}
