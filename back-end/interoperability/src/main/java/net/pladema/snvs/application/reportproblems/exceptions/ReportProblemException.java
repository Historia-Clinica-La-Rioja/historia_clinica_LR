package net.pladema.snvs.application.reportproblems.exceptions;

import lombok.Getter;

@Getter
public class ReportProblemException extends Exception {

    private ReportProblemEnumException code;

    public ReportProblemException(ReportProblemEnumException code, String message) {
        super(message);
        this.code = code;
    }
}

