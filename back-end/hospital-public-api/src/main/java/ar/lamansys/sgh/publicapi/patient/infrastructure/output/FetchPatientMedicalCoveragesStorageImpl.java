package ar.lamansys.sgh.publicapi.patient.infrastructure.output;

import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

import org.springframework.stereotype.Service;

import ar.lamansys.sgh.publicapi.patient.application.port.out.FetchPatientMedicalCoveragesStorage;
import ar.lamansys.sgh.publicapi.patient.domain.PatientMedicalCoverageBo;
import ar.lamansys.sgh.shared.infrastructure.input.service.SharedPatientMedicalCoverageDto;
import ar.lamansys.sgh.shared.infrastructure.input.service.SharedMedicalCoveragePort;
import lombok.AllArgsConstructor;

@AllArgsConstructor
@Service
public class FetchPatientMedicalCoveragesStorageImpl implements FetchPatientMedicalCoveragesStorage {

	private SharedMedicalCoveragePort sharedMedicalCoveragePort;

	@Override
	public Optional<List<PatientMedicalCoverageBo>> getMedicalCoverages(Integer patientId) {
		var result = sharedMedicalCoveragePort.fetchAllMedicalCoverages(patientId);
		return result.map(this::mapToBo);
	}

	private List<PatientMedicalCoverageBo> mapToBo(List<SharedPatientMedicalCoverageDto> fetchAllMedicalCoverages) {
		return fetchAllMedicalCoverages.stream()
				.map(mc -> PatientMedicalCoverageBo.builder().name(mc.getName())
						.type(mc.getType())
						.plan(mc.getPlan())
						.affiliateNumber(mc.getAffiliateNumber())
						.affiliationType(mc.getAffiliationType())
						.cuit(mc.getCuit())
						.startDate(mc.getStartDate())
						.endDate(mc.getEndDate()).build())
				.collect(Collectors.toList());
	}
}
