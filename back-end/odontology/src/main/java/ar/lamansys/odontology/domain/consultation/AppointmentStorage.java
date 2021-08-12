package ar.lamansys.odontology.domain.consultation;

import java.time.LocalDate;

public interface AppointmentStorage {

    Integer getPatientMedicalCoverageId(Integer patientId, Integer doctorId);

    void serveAppointment(Integer patientId, Integer doctorId, LocalDate date);

}
