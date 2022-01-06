package net.pladema.snvs.domain.report.exceptions;

import lombok.Getter;

@Getter
public class ReportCommandBoException extends Exception {

    private ReportCommandBoEnumException code;

    public ReportCommandBoException(ReportCommandBoEnumException code, String message) {
        super(message);
        this.code = code;
    }
}

