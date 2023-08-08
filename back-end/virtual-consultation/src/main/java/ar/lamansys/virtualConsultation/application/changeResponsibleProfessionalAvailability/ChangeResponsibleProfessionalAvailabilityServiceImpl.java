package ar.lamansys.virtualConsultation.application.changeResponsibleProfessionalAvailability;

import org.springframework.stereotype.Service;

import ar.lamansys.virtualConsultation.domain.VirtualConsultationResponsibleProfessionalAvailabilityBo;
import ar.lamansys.virtualConsultation.infrastructure.output.repository.VirtualConsultationResponsibleProfessionalAvailabilityRepository;
import ar.lamansys.virtualConsultation.infrastructure.output.repository.entity.VirtualConsultationResponsibleProfessionalAvailability;
import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;

@Service
@Slf4j
@AllArgsConstructor
public class ChangeResponsibleProfessionalAvailabilityServiceImpl implements ChangeResponsibleProfessionalAvailabilityService {

	private final VirtualConsultationResponsibleProfessionalAvailabilityRepository virtualConsultationResponsibleProfessionalAvailabilityRepository;

	@Override
	public boolean run(VirtualConsultationResponsibleProfessionalAvailabilityBo responsibleAvailability) {
		log.debug("Input parameters -> responsibleAvailability {}", responsibleAvailability);
		virtualConsultationResponsibleProfessionalAvailabilityRepository.save(mapToEntity(responsibleAvailability));
		return true;
	}

	private VirtualConsultationResponsibleProfessionalAvailability mapToEntity(VirtualConsultationResponsibleProfessionalAvailabilityBo professionalAvailability) {
		log.debug("Input parameters -> professionalAvailability {}", professionalAvailability);
		VirtualConsultationResponsibleProfessionalAvailability result = new VirtualConsultationResponsibleProfessionalAvailability(professionalAvailability);
		log.debug("Output -> {}", result);
		return result;
	}

}
