package net.pladema.clinichistory.hospitalization.application.anestheticreport.exceptions;

import lombok.Getter;
import net.pladema.clinichistory.hospitalization.domain.exceptions.AnestheticReportEnumException;

@Getter
public class AnestheticReportException extends RuntimeException {

    private final AnestheticReportEnumException code;

    public AnestheticReportException(AnestheticReportEnumException code, String message) {
        super(message);
        this.code = code;
    }

}
