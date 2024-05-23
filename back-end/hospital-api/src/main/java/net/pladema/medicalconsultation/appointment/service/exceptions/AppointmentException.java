package net.pladema.medicalconsultation.appointment.service.exceptions;

import ar.lamansys.sgh.shared.infrastructure.input.service.appointment.exceptions.SharedAppointmentException;
import lombok.Getter;

@Getter
public class AppointmentException extends RuntimeException implements SharedAppointmentException {

    private static final long serialVersionUID = -869671290283371644L;
    private final AppointmentEnumException code;

    public AppointmentException(AppointmentEnumException code, String errorMessage) {
        super(errorMessage);
        this.code = code;
    }

    public String getCode() {
        return code.name();
    }
}
