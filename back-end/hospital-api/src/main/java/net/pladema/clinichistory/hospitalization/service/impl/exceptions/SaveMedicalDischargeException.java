package net.pladema.clinichistory.hospitalization.service.impl.exceptions;

import lombok.Getter;

@Getter
public class SaveMedicalDischargeException extends RuntimeException{

    private final SaveMedicalDischargeExceptionEnum code;

    public SaveMedicalDischargeException(SaveMedicalDischargeExceptionEnum code, String message){
        super(message);
        this.code = code;
    }
}
