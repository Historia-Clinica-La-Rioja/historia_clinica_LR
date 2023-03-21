package net.pladema.medicalconsultation.equipmentdiary.service.domain;

import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

import java.time.LocalDate;
import java.util.List;

@Getter
@Setter
@ToString
public class EquipmentDiaryBo {

    protected Integer id;

    protected Integer equipmentId;


    protected LocalDate startDate;

    protected LocalDate endDate;

    protected Short appointmentDuration;

    protected boolean automaticRenewal = false;

    protected boolean includeHoliday = false;

    protected boolean active = true;

    protected List<EquipmentDiaryOpeningHoursBo> equipmentDiaryOpeningHours;
    
    protected boolean deleted = false;


}
