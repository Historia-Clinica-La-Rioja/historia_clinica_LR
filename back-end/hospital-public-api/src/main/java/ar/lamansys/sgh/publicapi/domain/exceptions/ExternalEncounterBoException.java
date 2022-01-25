package ar.lamansys.sgh.publicapi.domain.exceptions;

import lombok.Getter;

@Getter
public class ExternalEncounterBoException extends Exception {

    private ExternalEncounterBoEnumException code;

    public ExternalEncounterBoException(ExternalEncounterBoEnumException code, String message) {
        super(message);
        this.code = code;
    }
}