package ar.lamansys.odontology.domain.consultation;

import java.time.LocalDate;

public interface OdontologyAppointmentStorage {

    Integer getPatientMedicalCoverageId(Integer patientId, Integer doctorId);

    void serveAppointment(Integer patientId, Integer doctorId, LocalDate date);

}
