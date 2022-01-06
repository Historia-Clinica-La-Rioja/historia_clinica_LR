package ar.lamansys.refcounterref.application.port;

import java.time.LocalDate;

public interface CounterReferenceAppointmentStorage {

    void run(Integer patientId, Integer id, LocalDate nowDate);

    Integer getPatientMedicalCoverageId(Integer patientId, Integer id);

}