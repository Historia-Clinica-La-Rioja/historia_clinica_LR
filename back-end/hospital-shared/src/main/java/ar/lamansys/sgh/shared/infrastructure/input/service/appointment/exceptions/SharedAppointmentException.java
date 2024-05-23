package ar.lamansys.sgh.shared.infrastructure.input.service.appointment.exceptions;

public interface SharedAppointmentException {

    String getMessage();

    Throwable getCause();
}
