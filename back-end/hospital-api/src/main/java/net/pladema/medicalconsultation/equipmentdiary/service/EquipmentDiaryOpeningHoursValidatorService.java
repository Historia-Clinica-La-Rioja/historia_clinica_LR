package net.pladema.medicalconsultation.equipmentdiary.service;

import net.pladema.medicalconsultation.equipmentdiary.service.domain.EquipmentDiaryBo;
import net.pladema.medicalconsultation.equipmentdiary.service.domain.EquipmentDiaryOpeningHoursBo;

import java.time.LocalDate;
import java.util.List;

public interface EquipmentDiaryOpeningHoursValidatorService {


    boolean overlapDiaryOpeningHours(EquipmentDiaryBo equipmentDiaryBo, List<EquipmentDiaryOpeningHoursBo> openingHours);

	boolean allowNewOverturn(Integer equipmentDiaryId, Integer openingHoursId, LocalDate newApmtDate);
}

