package ar.lamansys.sgh.publicapi.patient.domain.exceptions;

import lombok.Getter;

@Getter
public class ExternalPatientExtendedBoException extends Exception {

    private ExternalPatientExtendedBoEnumException code;

    public ExternalPatientExtendedBoException(ExternalPatientExtendedBoEnumException code, String message) {
        super(message);
        this.code = code;
    }

}
