package ar.lamansys.sgh.clinichistory.application.anestheticreport.exceptions;

import ar.lamansys.sgh.clinichistory.domain.document.enums.EAnestheticReportException;
import lombok.Getter;

@Getter
public class AnestheticReportException extends RuntimeException {

    private static final long serialVersionUID = 6151250753930430220L;
    private final EAnestheticReportException code;

    public AnestheticReportException(EAnestheticReportException code, String message) {
        super(message);
        this.code = code;
    }

}
