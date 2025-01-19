package ar.lamansys.sgh.publicapi.activities.medicalcoverages.application.port.out;

import ar.lamansys.sgh.shared.infrastructure.input.service.MedicalCoverageDataDto;

import java.util.List;

public interface FetchAllMedicalCoveragesStorage {
	List<MedicalCoverageDataDto> fetchAll();
}
