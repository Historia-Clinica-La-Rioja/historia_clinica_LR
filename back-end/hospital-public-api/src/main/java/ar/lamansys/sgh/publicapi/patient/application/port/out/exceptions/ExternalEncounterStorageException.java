package ar.lamansys.sgh.publicapi.patient.application.port.out.exceptions;

import lombok.Getter;

@Getter
public class ExternalEncounterStorageException extends RuntimeException {

    public final ExternalEncounterStorageExceptionEnum code;

    public ExternalEncounterStorageException(ExternalEncounterStorageExceptionEnum code, String message) {
        super(message);
        this.code = code;
    }
}
