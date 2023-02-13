package net.pladema.medicalconsultation.equipmentdiary.service;


import net.pladema.medicalconsultation.diary.service.domain.OccupationBo;
import net.pladema.medicalconsultation.equipmentdiary.service.domain.EquipmentDiaryOpeningHoursBo;
import net.pladema.medicalconsultation.equipmentdiary.service.exception.EquipmentDiaryOpeningHoursException;

import javax.validation.constraints.NotNull;
import java.time.LocalDate;
import java.util.Collection;
import java.util.List;

public interface EquipmentDiaryOpeningHoursService {

    void load(Integer equipmentDiaryId, List<EquipmentDiaryOpeningHoursBo> equipmentDiaryOpeningHour);
    
    void update(Integer diaryId, List<EquipmentDiaryOpeningHoursBo> equipmentDiaryOpeningHours);

	List<OccupationBo> findAllWeeklyEquipmentOccupation(Integer equipmentId, LocalDate startDate, LocalDate endDate, Integer ignoreDiaryId) throws EquipmentDiaryOpeningHoursException;

	List<Short> overlappingDays(@NotNull LocalDate rangeStart1, @NotNull LocalDate rangeEnd1,
								@NotNull LocalDate rangeStart2, @NotNull LocalDate rangeEnd2);

	Collection<EquipmentDiaryOpeningHoursBo> getDiariesOpeningHours(List<Integer> equipmentDiaryIds);

}
