package net.pladema.snvs.application.ports.report.exceptions;

import lombok.Getter;

@Getter
public class ReportPortException extends Exception {

    private ReportPortEnumException code;

    public ReportPortException(ReportPortEnumException code, String message) {
        super(message);
        this.code = code;
    }
}

