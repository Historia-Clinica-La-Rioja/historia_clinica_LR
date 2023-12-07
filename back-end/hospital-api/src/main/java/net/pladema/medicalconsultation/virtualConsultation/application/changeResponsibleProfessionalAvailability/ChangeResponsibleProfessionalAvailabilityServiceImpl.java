package net.pladema.medicalconsultation.virtualConsultation.application.changeResponsibleProfessionalAvailability;

import org.springframework.stereotype.Service;

import net.pladema.medicalconsultation.virtualConsultation.domain.VirtualConsultationResponsibleProfessionalAvailabilityBo;
import net.pladema.medicalconsultation.virtualConsultation.infrastructure.output.repository.VirtualConsultationResponsibleProfessionalAvailabilityRepository;
import net.pladema.medicalconsultation.virtualConsultation.infrastructure.output.repository.entity.VirtualConsultationResponsibleProfessionalAvailability;
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
