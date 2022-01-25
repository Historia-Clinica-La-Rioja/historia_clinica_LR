package ar.lamansys.sgh.publicapi.application.saveexternalencounter.exceptions;

import lombok.Getter;

@Getter
public class SaveExternalEncounterException extends RuntimeException {

    public final SaveExternalEncounterEnumException code;

    public SaveExternalEncounterException(SaveExternalEncounterEnumException code, String message) {
        super(message);
        this.code = code;
    }
}