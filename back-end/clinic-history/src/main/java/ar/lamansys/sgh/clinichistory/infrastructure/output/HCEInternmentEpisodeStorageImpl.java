package ar.lamansys.sgh.clinichistory.infrastructure.output;

import java.util.Optional;

import org.springframework.stereotype.Service;

import ar.lamansys.sgh.clinichistory.application.ports.HCEInternmenEpisodeStorage;
import ar.lamansys.sgh.shared.infrastructure.input.service.ExternalPatientCoverageDto;
import ar.lamansys.sgh.shared.infrastructure.input.service.SharedHospitalizationPort;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

@Service
@RequiredArgsConstructor
@Slf4j
public class HCEInternmentEpisodeStorageImpl implements HCEInternmenEpisodeStorage {

	private final SharedHospitalizationPort sharedHospitalizationPort;

	@Override
	public Optional<ExternalPatientCoverageDto> getActiveEpisodeMedicalCoverage(Integer internmentEpisodeId) {
		log.debug("Input parameters -> internmentEpisodeId {}", internmentEpisodeId);
		Optional<ExternalPatientCoverageDto> result = sharedHospitalizationPort.getActiveEpisodeMedicalCoverage(internmentEpisodeId);
		log.debug("Output -> {}", result);
		return result;
	}
}
