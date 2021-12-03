package ar.lamansys.sgh.publicapi.domain.exceptions;

import lombok.Getter;

@Getter
public class ExternalPatientBoException extends Exception {

    private ExternalPatientBoEnumException code;

    public ExternalPatientBoException(ExternalPatientBoEnumException code, String message) {
        super(message);
        this.code = code;
    }

}
