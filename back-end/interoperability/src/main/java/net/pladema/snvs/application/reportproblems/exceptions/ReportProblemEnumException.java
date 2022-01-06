package net.pladema.snvs.application.reportproblems.exceptions;

import lombok.Getter;

@Getter
public enum ReportProblemEnumException {
    UNKNOWN_PATIENT, UNKNOWN_INSTITUTION, UNKNOWN_EVENT, NULL_REPORT, UNKNOWN_PROFESSIONAL;
}
