package ar.lamansys.sgh.shared.infrastructure.input.service;


import java.time.LocalDate;

public interface SharedAppointmentPort {

    boolean hasConfirmedAppointment(Integer patientId, Integer doctorId, LocalDate date);

    void serveAppointment(Integer patientId, Integer doctorId, LocalDate date);
}
