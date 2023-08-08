package ar.lamansys.virtualConsultation.application.getResponsibleProfessionalAvailability;

import java.util.Optional;

import org.springframework.stereotype.Service;

import ar.lamansys.virtualConsultation.domain.VirtualConsultationResponsibleProfessionalAvailabilityBo;
import ar.lamansys.virtualConsultation.infrastructure.output.repository.VirtualConsultationResponsibleProfessionalAvailabilityRepository;
import ar.lamansys.virtualConsultation.infrastructure.output.repository.entity.VirtualConsultationResponsibleProfessionalAvailability;
import ar.lamansys.virtualConsultation.infrastructure.output.repository.entity.VirtualConsultationResponsibleProfessionalAvailabilityPK;
import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;

@Service
@Slf4j
@AllArgsConstructor
public class GetResponsibleProfessionalAvailabilityServiceImpl implements GetResponsibleProfessionalAvailabilityService {

	private final VirtualConsultationResponsibleProfessionalAvailabilityRepository virtualConsultationResponsibleProfessionalAvailabilityRepository;

	@Override
	public VirtualConsultationResponsibleProfessionalAvailabilityBo run(Integer healthcareProfessionalId, Integer institutionId) {
		log.debug("Input parameters -> healthcareProfessionalId {}, institutionId {}", healthcareProfessionalId, institutionId);
		Optional<VirtualConsultationResponsibleProfessionalAvailability> virtualConsultationUserAvailability = virtualConsultationResponsibleProfessionalAvailabilityRepository
				.findById(new VirtualConsultationResponsibleProfessionalAvailabilityPK(healthcareProfessionalId, institutionId));
        return virtualConsultationUserAvailability.map(this::mapFromEntity).orElse(null);
    }

	private VirtualConsultationResponsibleProfessionalAvailabilityBo mapFromEntity(VirtualConsultationResponsibleProfessionalAvailability professionalAvailability) {
		log.debug("Input parameters -> professionalAvailability {}", professionalAvailability);
		VirtualConsultationResponsibleProfessionalAvailabilityBo result = new VirtualConsultationResponsibleProfessionalAvailabilityBo(professionalAvailability.getId().getHealthcareProfessionalId(), professionalAvailability.getId().getInstitutionId());
		result.setAvailable(professionalAvailability.getAvailable());
		log.debug("Output -> {}", result);
		return result;
	}

}
