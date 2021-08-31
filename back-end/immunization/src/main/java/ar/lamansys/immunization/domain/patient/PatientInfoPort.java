package ar.lamansys.immunization.domain.patient;

import java.util.Optional;

public interface PatientInfoPort {

    Optional<PatientInfoBo> getPatientInfo(Integer patientId);
}
