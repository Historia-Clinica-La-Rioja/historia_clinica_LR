package ar.lamansys.virtualConsultation.application.getResponsibleProfesionalAvailability;

import org.springframework.stereotype.Service;

import ar.lamansys.virtualConsultation.infrastructure.output.repository.VirtualConsultationResponsibleProfessionalAvailabilityRepository;
import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;

@Slf4j
@AllArgsConstructor
@Service
public class GetResponsibleProfessionalAvailabilityServiceImpl implements GetResponsibleProfessionalAvailabilityService{

	private final VirtualConsultationResponsibleProfessionalAvailabilityRepository virtualConsultationResponsibleProfessionalAvailabilityRepository;

	@Override
	public Boolean run(Integer healthcareProfessionalId, Integer institutionId) {
		log.debug("Input parameter -> healthcareProfessionalId {}, institutionId {}", healthcareProfessionalId, institutionId);
		Boolean result = virtualConsultationResponsibleProfessionalAvailabilityRepository.getAvailabilityByHealthcareProfessionalId(healthcareProfessionalId, institutionId);
		log.debug("Output -> {}", result);
		return result;
	}

}
