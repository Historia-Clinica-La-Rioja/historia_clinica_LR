package ar.lamansys.sgh.publicapi.application.deleteexternalencounter.exceptions;

import lombok.Getter;

@Getter
public class DeleteExternalEncounterException extends RuntimeException {

    public final DeleteExternalEncounterEnumException code;

    public DeleteExternalEncounterException(DeleteExternalEncounterEnumException code, String message) {
        super(message);
        this.code = code;
    }
}