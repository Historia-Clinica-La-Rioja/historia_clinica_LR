package net.pladema.medicalconsultation.equipmentdiary.service;

import net.pladema.medicalconsultation.diary.service.domain.DiaryOpeningHoursBo;
import net.pladema.medicalconsultation.diary.service.domain.OccupationBo;
import net.pladema.medicalconsultation.diary.service.exception.DiaryOpeningHoursException;
import net.pladema.medicalconsultation.equipmentdiary.service.domain.EquipmentDiaryOpeningHoursBo;

import javax.validation.constraints.NotNull;
import java.time.LocalDate;
import java.util.Collection;
import java.util.List;

public interface EquipmentDiaryOpeningHoursService {

    void load(Integer diaryId, List<EquipmentDiaryOpeningHoursBo> diaryOpeningHours, List<EquipmentDiaryOpeningHoursBo>... oldOpeningHours);
    
    void update(Integer diaryId, List<EquipmentDiaryOpeningHoursBo> diaryOpeningHours);


}
