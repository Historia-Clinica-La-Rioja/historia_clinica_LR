package ar.lamansys.sgh.publicapi.activities.medicalcoverages.infrastructure.output;

import ar.lamansys.sgh.publicapi.activities.medicalcoverages.application.port.out.FetchAllMedicalCoveragesStorage;

import ar.lamansys.sgh.shared.infrastructure.input.service.MedicalCoverageDataDto;
import ar.lamansys.sgh.shared.infrastructure.input.service.SharedMedicalCoveragePort;

import lombok.AllArgsConstructor;

import org.springframework.stereotype.Service;

import java.util.List;

@AllArgsConstructor
@Service
public class FetchAllMedicalCoveragesStorageImpl implements FetchAllMedicalCoveragesStorage {

	private SharedMedicalCoveragePort sharedMedicalCoveragePort;

	@Override
	public List<MedicalCoverageDataDto> fetchAll() {
		return sharedMedicalCoveragePort.fetchAllMedicalCoverages();
	}
}
