package net.pladema.medicalconsultation.equipmentdiary.service.exception;

import lombok.Getter;

@Getter
public class EquipmentDiaryOpeningHoursException extends Exception {

    private final EEquipmentDiaryOpeningHoursEnumException code;

    public EquipmentDiaryOpeningHoursException(EEquipmentDiaryOpeningHoursEnumException code, String  messageError) {
        super(messageError);
        this.code = code;
    }
}