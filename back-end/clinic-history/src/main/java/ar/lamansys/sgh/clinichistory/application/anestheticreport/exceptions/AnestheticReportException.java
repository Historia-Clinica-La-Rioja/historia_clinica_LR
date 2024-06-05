package ar.lamansys.sgh.clinichistory.application.anestheticreport.exceptions;

import ar.lamansys.sgh.clinichistory.domain.document.exceptions.AnestheticReportEnumException;
import lombok.Getter;

@Getter
public class AnestheticReportException extends RuntimeException {

    private static final long serialVersionUID = 6151250753930430220L;
    private final AnestheticReportEnumException code;

    public AnestheticReportException(AnestheticReportEnumException code, String message) {
        super(message);
        this.code = code;
    }

}
