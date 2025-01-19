package ar.lamansys.sgh.clinichistory.application.signDocumentFile.exceptions;

import lombok.Getter;

@Getter
public class SignDocumentFileException extends RuntimeException {

    private final SignDocumentFileEnumException code;

    public SignDocumentFileException(SignDocumentFileEnumException code, String message) {
        super(message);
        this.code = code;
    }
}