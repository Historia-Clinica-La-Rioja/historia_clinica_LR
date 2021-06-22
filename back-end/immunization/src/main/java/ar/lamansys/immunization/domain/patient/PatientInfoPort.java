package ar.lamansys.immunization.domain.patient;

import java.util.Optional;

public interface PatientInfoPort {

    Optional<PatientInfoBO> getPatientInfo(Integer patientId);
}
