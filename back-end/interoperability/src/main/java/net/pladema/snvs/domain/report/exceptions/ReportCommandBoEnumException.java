package net.pladema.snvs.domain.report.exceptions;

import lombok.Getter;

@Getter
public enum ReportCommandBoEnumException {
    NULL_PATIENT_ID,
    NULL_INSTITUTION_ID,
    NULL_MANUAL_CLASSIFICATION_ID,
    NULL_PROBLEM,
    NULL_GROUP_EVENT_ID,
    NULL_PROFESSIONAL_ID,
    NULL_EVENT_ID;
}
