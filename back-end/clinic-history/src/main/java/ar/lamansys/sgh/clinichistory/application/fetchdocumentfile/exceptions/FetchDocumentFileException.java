package ar.lamansys.sgh.clinichistory.application.fetchdocumentfile.exceptions;

import lombok.Getter;

@Getter
public class FetchDocumentFileException extends RuntimeException {

    private final FetchDocumentFileExceptionEnum code;

    public FetchDocumentFileException(FetchDocumentFileExceptionEnum code, String message) {
        super(message);
        this.code = code;
    }
}