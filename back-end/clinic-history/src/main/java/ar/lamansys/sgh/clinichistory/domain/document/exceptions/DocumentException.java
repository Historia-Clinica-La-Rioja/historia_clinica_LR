package ar.lamansys.sgh.clinichistory.domain.document.exceptions;

import ar.lamansys.sgh.clinichistory.domain.document.enums.EDocumentException;
import lombok.Getter;

@Getter
public class DocumentException extends RuntimeException {

    private static final long serialVersionUID = 6151250753930430222L;
    private final EDocumentException code;

    public DocumentException(EDocumentException code, String message) {
        super(message);
        this.code = code;
    }
}
