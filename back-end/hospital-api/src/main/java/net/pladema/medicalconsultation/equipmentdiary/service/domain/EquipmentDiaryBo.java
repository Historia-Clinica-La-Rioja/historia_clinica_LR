package net.pladema.medicalconsultation.equipmentdiary.service.domain;

import java.time.LocalDate;
import java.util.List;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.ToString;

@Getter
@Setter
@ToString
@NoArgsConstructor
public class EquipmentDiaryBo {

    protected Integer id;

    protected Integer equipmentId;

    protected LocalDate startDate;

    protected LocalDate endDate;

    protected Short appointmentDuration;

    protected boolean automaticRenewal = false;

    protected boolean includeHoliday = false;

    protected boolean active = true;

    protected List<EquipmentDiaryOpeningHoursBo> diaryOpeningHours;
    
    protected boolean deleted = false;

}
