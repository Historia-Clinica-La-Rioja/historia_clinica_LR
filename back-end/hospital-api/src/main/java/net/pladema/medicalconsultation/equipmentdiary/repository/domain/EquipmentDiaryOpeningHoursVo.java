package net.pladema.medicalconsultation.equipmentdiary.repository.domain;

import lombok.Getter;
import lombok.ToString;
import lombok.Value;
import net.pladema.medicalconsultation.diary.repository.entity.OpeningHours;

@Getter
@ToString
@Value
public class EquipmentDiaryOpeningHoursVo {

    private final Integer equipmentDiaryId;

    private final OpeningHours openingHours;

    private final Short medicalAttentionTypeId;

    private final Short overturnCount;

    private final Boolean externalAppointmentsAllowed;

    public EquipmentDiaryOpeningHoursVo(Integer equipmentDiaryId, OpeningHours op, Short medicalAttentionTypeId, Short overturnCount, Boolean externalAppointmentsAllowed){
        this.equipmentDiaryId = equipmentDiaryId;
        this.openingHours = op;
        this.medicalAttentionTypeId = medicalAttentionTypeId;
        this.overturnCount = overturnCount;
        this.externalAppointmentsAllowed = externalAppointmentsAllowed;
    }
}
