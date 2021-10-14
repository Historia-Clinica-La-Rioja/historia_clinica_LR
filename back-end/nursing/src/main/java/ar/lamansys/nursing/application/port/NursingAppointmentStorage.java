package ar.lamansys.nursing.application.port;

import java.time.LocalDate;

public interface NursingAppointmentStorage {

    void run(Integer patientId, Integer id, LocalDate nowDate);

    Integer getPatientMedicalCoverageId(Integer patientId, Integer id);

}
