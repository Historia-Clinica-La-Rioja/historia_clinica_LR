package net.pladema.clinichistory.outpatient.application.markaserroraproblem.exceptions;

import lombok.Getter;

@Getter
public enum MarkAsErrorAProblemExceptionEnum {
    FORBIDDEN_USER_ID,
    TIME_WINDOW_EXPIRATION,
    HAS_AT_LEAST_ONE_STUDY_COMPLETED,
    HAS_AT_LEAST_ONE_APPOINTMENT_CONFIRMED_OR_SERVED,
    NOT_ACTIVE_OR_CHRONIC,
    REFERENCE_ADVANCED_STATE,
}
