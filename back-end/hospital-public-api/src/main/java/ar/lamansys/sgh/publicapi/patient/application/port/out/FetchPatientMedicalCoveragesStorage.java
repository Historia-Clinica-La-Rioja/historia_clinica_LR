package ar.lamansys.sgh.publicapi.patient.application.port.out;

import java.util.List;
import java.util.Optional;

import ar.lamansys.sgh.publicapi.patient.domain.PatientMedicalCoverageBo;

public interface FetchPatientMedicalCoveragesStorage {
	Optional<List<PatientMedicalCoverageBo>> getMedicalCoverages(Integer patientId);
}
