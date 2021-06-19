package ar.lamansys.sgh.clinichistory.domain.document.event.exceptions;

import lombok.Getter;

@Getter
public class GenerateDocumentEventException extends RuntimeException {

    private final GenerateDocumentEventEnumException code;

    public GenerateDocumentEventException(GenerateDocumentEventEnumException code, String message) {
        super(message);
        this.code = code;
    }
}