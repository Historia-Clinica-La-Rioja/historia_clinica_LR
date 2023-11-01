package net.pladema.clinichistory.outpatient.application.exceptions;

import lombok.Getter;

@Getter
public enum MarkAsErrorAProblemExceptionEnum {
    FORBIDDEN_USER_ID,
    TIME_WINDOW_EXPIRATION
    ;
}
