package net.pladema.sgx.healthinsurance.infrastructure.input.shared;

import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

import org.springframework.stereotype.Service;

import ar.lamansys.sgh.shared.infrastructure.input.service.MedicalCoverageDataDto;
import ar.lamansys.sgh.shared.infrastructure.input.service.SharedMedicalCoveragePort;
import lombok.AllArgsConstructor;
import net.pladema.patient.controller.dto.EMedicalCoverageType;
import net.pladema.patient.service.domain.ARTCoverageBo;
import net.pladema.patient.service.domain.PrivateHealthInsuranceBo;
import net.pladema.renaper.services.domain.PersonMedicalCoverageBo;
import net.pladema.sgx.healthinsurance.service.ARTCoverageService;
import net.pladema.sgx.healthinsurance.service.HealthInsuranceService;
import net.pladema.sgx.healthinsurance.service.PrivateHealthInsuranceService;

@AllArgsConstructor
@Service
public class SharedMedicalCoveragePortImpl implements SharedMedicalCoveragePort {

	private HealthInsuranceService healthInsuranceService;
	private PrivateHealthInsuranceService privateHealthInsuranceService;
	private ARTCoverageService artCoverageService;

	@Override
	public List<MedicalCoverageDataDto> fetchAllMedicalCoverages() {
		var healthInsurances = healthInsuranceService.getAll();
		var privateHealthInsurances = privateHealthInsuranceService.getAll();
		var artCoverages = artCoverageService.getAll();

		List<MedicalCoverageDataDto> result = new ArrayList<>();

		result.addAll(healthInsurances.stream()
				.map(this::mapTo)
				.collect(Collectors.toList()));

		result.addAll(privateHealthInsurances.stream()
				.map(this::mapTo)
				.collect(Collectors.toList()));

		result.addAll(artCoverages.stream()
				.map(this::mapTo)
				.collect(Collectors.toList()));

		return result;
	}

	private MedicalCoverageDataDto mapTo(PersonMedicalCoverageBo hi) {
		return MedicalCoverageDataDto.builder()
				.name(hi.getName())
				.cuit(hi.getCuit())
				.type(EMedicalCoverageType.OBRASOCIAL.getValue())
				.rnos(hi.getRnos())
				.build();
	}

	private MedicalCoverageDataDto mapTo(PrivateHealthInsuranceBo phi) {
		return MedicalCoverageDataDto.builder()
				.name(phi.getName())
				.cuit(phi.getCuit())
				.type(EMedicalCoverageType.PREPAGA.getValue())
				.build();
	}

	private MedicalCoverageDataDto mapTo(ARTCoverageBo art) {
		return MedicalCoverageDataDto.builder()
				.name(art.getName())
				.cuit(art.getCuit())
				.type(EMedicalCoverageType.ART.getValue())
				.build();
	}
}