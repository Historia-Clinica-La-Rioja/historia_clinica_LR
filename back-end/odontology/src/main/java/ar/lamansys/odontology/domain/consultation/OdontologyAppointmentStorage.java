package ar.lamansys.odontology.domain.consultation;

import java.time.LocalDate;

public interface OdontologyAppointmentStorage {

    Integer getPatientMedicalCoverageId(Integer patientId, Integer doctorId);

    Integer serveAppointment(Integer patientId, Integer doctorId, LocalDate date);

}
