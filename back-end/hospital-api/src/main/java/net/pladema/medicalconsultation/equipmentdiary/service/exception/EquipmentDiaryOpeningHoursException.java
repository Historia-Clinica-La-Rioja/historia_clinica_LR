package net.pladema.medicalconsultation.equipmentdiary.service.exception;

import lombok.Getter;
import net.pladema.medicalconsultation.diary.service.exception.DiaryOpeningHoursEnumException;

@Getter
public class EquipmentDiaryOpeningHoursException extends Exception {

    private final EEquipmentDiaryOpeningHoursEnumException code;

    public EquipmentDiaryOpeningHoursException(EEquipmentDiaryOpeningHoursEnumException code, String mensajeError) {
        super(mensajeError);
        this.code = code;
    }
}