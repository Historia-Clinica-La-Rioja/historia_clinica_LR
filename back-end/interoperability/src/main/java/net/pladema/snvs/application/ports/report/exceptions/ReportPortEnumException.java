package net.pladema.snvs.application.ports.report.exceptions;

import lombok.Getter;

@Getter
public enum ReportPortEnumException {
    UNKNOWN_PATIENT, UNKNOWN_INSTITUTION, NULL_REPORT;
}
