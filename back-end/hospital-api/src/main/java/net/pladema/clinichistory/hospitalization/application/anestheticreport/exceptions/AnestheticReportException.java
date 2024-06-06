package net.pladema.clinichistory.hospitalization.application.anestheticreport.exceptions;

import lombok.Getter;
import net.pladema.clinichistory.hospitalization.domain.exceptions.AnestheticReportEnumException;

@Getter
public class AnestheticReportException extends RuntimeException {

    private static final long serialVersionUID = 6151250753930430220L;
    private final AnestheticReportEnumException code;

    public AnestheticReportException(AnestheticReportEnumException code, String message) {
        super(message);
        this.code = code;
    }

}
