package ar.lamansys.virtualConsultation.application.changeClinicalProfessionalAvailability;

import org.springframework.stereotype.Service;

import ar.lamansys.virtualConsultation.domain.ClinicalProfessionalAvailabilityBo;
import ar.lamansys.virtualConsultation.infrastructure.output.repository.ClinicalProfessionalAvailabilityRepository;
import ar.lamansys.virtualConsultation.infrastructure.output.repository.entity.VirtualConsultationClinicalProfessionalAvailability;
import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;

@AllArgsConstructor
@Slf4j
@Service
public class ChangeClinicalProfessionalAvailabilityServiceImpl implements  ChangeClinicalProfessionalAvailabilityService {

	private final ClinicalProfessionalAvailabilityRepository clinicalProfessionalAvailabilityRepository;

	@Override
	public Boolean run(ClinicalProfessionalAvailabilityBo professionalAvailability) {
		log.debug("Input parameters -> professionalAvailability {}", professionalAvailability);
		clinicalProfessionalAvailabilityRepository.save(new VirtualConsultationClinicalProfessionalAvailability(professionalAvailability));
		return true;
	}

}
