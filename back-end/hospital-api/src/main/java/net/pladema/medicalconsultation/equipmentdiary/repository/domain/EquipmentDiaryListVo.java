package net.pladema.medicalconsultation.equipmentdiary.repository.domain;

import lombok.*;
import net.pladema.medicalconsultation.equipmentdiary.repository.entity.EquipmentDiary;

import java.time.LocalDate;


@ToString
@AllArgsConstructor
@Getter
@Setter
@EqualsAndHashCode
public class EquipmentDiaryListVo {

    private final Integer id;

    private final Integer equipmentId;

    private final LocalDate startDate;

    private final LocalDate endDate;

    private final Short appointmentDuration;

    private final boolean automaticRenewal;


    private final boolean includeHoliday;

    public EquipmentDiaryListVo(EquipmentDiary equipmentDiary) {
        this.id = equipmentDiary.getId();
        this.equipmentId = equipmentDiary.getEquipmentId();
        this.startDate = equipmentDiary.getStartDate();
        this.endDate = equipmentDiary.getEndDate();
        this.appointmentDuration = equipmentDiary.getAppointmentDuration();
        this.automaticRenewal = equipmentDiary.isAutomaticRenewal();
        this.includeHoliday = equipmentDiary.isIncludeHoliday();
    }

}
