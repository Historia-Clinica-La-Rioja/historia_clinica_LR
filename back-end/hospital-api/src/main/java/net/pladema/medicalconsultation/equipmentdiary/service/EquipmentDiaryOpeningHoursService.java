package net.pladema.medicalconsultation.equipmentdiary.service;

import net.pladema.medicalconsultation.diary.service.domain.DiaryOpeningHoursBo;
import net.pladema.medicalconsultation.diary.service.domain.OccupationBo;
import net.pladema.medicalconsultation.diary.service.exception.DiaryOpeningHoursException;
import net.pladema.medicalconsultation.equipmentdiary.service.domain.EquipmentDiaryOpeningHoursBo;
import net.pladema.medicalconsultation.equipmentdiary.service.exception.EquipmentDiaryOpeningHoursException;

import javax.validation.constraints.NotNull;
import java.time.LocalDate;
import java.util.Collection;
import java.util.List;

public interface EquipmentDiaryOpeningHoursService {

    void load(Integer diaryId, List<EquipmentDiaryOpeningHoursBo> diaryOpeningHours, List<EquipmentDiaryOpeningHoursBo>... oldOpeningHours);
    
    void update(Integer diaryId, List<EquipmentDiaryOpeningHoursBo> diaryOpeningHours);

	List<OccupationBo> findAllWeeklyEquipmentOccupation(Integer equipmentId, LocalDate startDate, LocalDate endDate, Integer ignoreDiaryId) throws EquipmentDiaryOpeningHoursException;

	List<Short> overlappingDays(@NotNull LocalDate rangeStart1, @NotNull LocalDate rangeEnd1,
								@NotNull LocalDate rangeStart2, @NotNull LocalDate rangeEnd2);

}
