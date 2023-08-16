package ar.lamansys.virtualConsultation.application.getProfessionalAvailabilityService;

import org.springframework.stereotype.Service;

import ar.lamansys.virtualConsultation.infrastructure.output.repository.ClinicalProfessionalAvailabilityRepository;
import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;

@Slf4j
@AllArgsConstructor
@Service
public class GetProfessionalAvailabilityServiceImpl implements GetProfessionalAvailabilityService {

	private final ClinicalProfessionalAvailabilityRepository clinicalProfessionalAvailabilityRepository;

	@Override
	public Boolean run(Integer healthcareProfessionalId) {
		log.debug("Input parameters -> healthcareProfessionalId {}", healthcareProfessionalId);
		Boolean result = clinicalProfessionalAvailabilityRepository.getProfessionalAvailabilityByHealthcareProfessionalId(healthcareProfessionalId);
		log.debug("Output -> {}", result);
		return result;
	}

}
