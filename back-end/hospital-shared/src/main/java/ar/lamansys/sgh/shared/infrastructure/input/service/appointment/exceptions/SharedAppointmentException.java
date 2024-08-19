package ar.lamansys.sgh.shared.infrastructure.input.service.appointment.exceptions;

public class SharedAppointmentException extends RuntimeException {
    public SharedAppointmentException(String errorMessage) {
        super(errorMessage);
    }
}
