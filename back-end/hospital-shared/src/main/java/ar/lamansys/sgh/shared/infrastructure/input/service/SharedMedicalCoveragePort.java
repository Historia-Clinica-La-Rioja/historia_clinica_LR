package ar.lamansys.sgh.shared.infrastructure.input.service;

import java.util.List;
import java.util.Optional;

public interface SharedMedicalCoveragePort {
	List<MedicalCoverageDataDto> fetchAllMedicalCoverages();

	Optional<List<SharedPatientMedicalCoverageDto>> fetchAllMedicalCoverages(Integer patientId);
}
