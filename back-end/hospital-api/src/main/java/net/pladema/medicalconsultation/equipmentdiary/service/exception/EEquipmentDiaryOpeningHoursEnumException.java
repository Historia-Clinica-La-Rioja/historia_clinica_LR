package net.pladema.medicalconsultation.equipmentdiary.service.exception;

import lombok.Getter;

@Getter
public enum EEquipmentDiaryOpeningHoursEnumException {
    NULL_EQUIPMENT_ID,
    DIARY_END_DATE_BEFORE_START_DATE,
    ;

}
