package net.pladema.clinichistory.hospitalization.infrastructure.input.shared;

import java.util.Optional;

import org.springframework.stereotype.Service;

import ar.lamansys.sgh.shared.infrastructure.input.service.ExternalCoverageDto;
import ar.lamansys.sgh.shared.infrastructure.input.service.ExternalPatientCoverageDto;
import ar.lamansys.sgh.shared.infrastructure.input.service.SharedHospitalizationPort;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import net.pladema.clinichistory.hospitalization.service.InternmentEpisodeService;
import net.pladema.patient.service.domain.MedicalCoverageBo;
import net.pladema.patient.service.domain.PatientMedicalCoverageBo;

@Service
@Slf4j
@RequiredArgsConstructor
public class SharedHospitalizationPortImpl implements SharedHospitalizationPort {

	private final InternmentEpisodeService internmentEpisodeService;

	@Override
	public Optional<ExternalPatientCoverageDto> getActiveEpisodeMedicalCoverage(Integer internmentEpisodeId) {
		log.debug("Input parameters -> internmentEpisodeId {}", internmentEpisodeId);
		Optional<ExternalPatientCoverageDto> result = internmentEpisodeService.getMedicalCoverage(internmentEpisodeId).map(this::mapToExternalPatientCoverageDto);
		log.debug("Output -> {}", result);
		return result;
	}

	private ExternalPatientCoverageDto mapToExternalPatientCoverageDto(PatientMedicalCoverageBo bo) {
		MedicalCoverageBo medicalCoverage = bo.getMedicalCoverage();
		String type = (bo.getMedicalCoverage().getClass().getSimpleName().equals("PrivateHealthInsuranceBo"))?"PREPAGA":"OBRASOCIAL";
		return new ExternalPatientCoverageDto(new ExternalCoverageDto(medicalCoverage.getId(), medicalCoverage.getCuit(), bo.getPrivateHealthInsuranceDetails().getPlanName(), medicalCoverage.getName(), type), bo.getAffiliateNumber(), bo.getActive(), bo.getVigencyDate());
	}
}
