package ar.lamansys.odontology.application.odontogram.ports;

import java.time.LocalDate;

public interface OdontologyAppointmentStorage {

    Integer getPatientMedicalCoverageId(Integer patientId, Integer doctorId);

    Integer serveAppointment(Integer patientId, Integer doctorId, LocalDate date);

}
