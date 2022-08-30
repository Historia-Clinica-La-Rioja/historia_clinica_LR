package ar.lamansys.immunization.domain.appointment;

import java.time.LocalDate;

public interface AppointmentStorage {

    Integer run(Integer patientId, Integer id, LocalDate nowDate);

    Integer getPatientMedicalCoverageId(Integer patientId, Integer id);
}
