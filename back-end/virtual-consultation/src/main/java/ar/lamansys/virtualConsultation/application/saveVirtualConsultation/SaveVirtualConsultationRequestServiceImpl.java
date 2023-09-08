package ar.lamansys.virtualConsultation.application.saveVirtualConsultation;

import java.util.UUID;

import net.pladema.establishment.repository.CareLineInstitutionRepository;

import org.springframework.stereotype.Service;

import ar.lamansys.virtualConsultation.domain.VirtualConsultationRequestBo;
import ar.lamansys.virtualConsultation.infrastructure.output.repository.VirtualConsultationRepository;
import ar.lamansys.virtualConsultation.infrastructure.output.repository.entity.VirtualConsultation;
import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;

import org.springframework.util.Assert;

@Service
@Slf4j
@AllArgsConstructor
public class SaveVirtualConsultationRequestServiceImpl implements SaveVirtualConsultationRequestService {

	private final VirtualConsultationRepository virtualConsultationRepository;

	private final CareLineInstitutionRepository careLineInstitutionRepository;

	@Override
	public Integer run(VirtualConsultationRequestBo virtualConsultation) {
		log.debug("Input parameters -> virtualConsultation {}", virtualConsultation);
		validateVirtualConsultationRequest(virtualConsultation);
		Integer result = virtualConsultationRepository.save(mapToEntity(virtualConsultation)).getId();
		log.debug("Output -> {}", result);
		return result;
	}

	private void validateVirtualConsultationRequest(VirtualConsultationRequestBo virtualConsultation) {
		Assert.notNull(careLineInstitutionRepository.careLineIsAvailableInInstitution(virtualConsultation.getCareLineId(), virtualConsultation.getInstitutionId()), "La linea de cuidado seleccionada no pertenece a la institución");
	}

	private VirtualConsultation mapToEntity(VirtualConsultationRequestBo virtualConsultationRequestBo) {
		VirtualConsultation virtualConsultation = new VirtualConsultation(virtualConsultationRequestBo);
		virtualConsultation.setCallId(UUID.randomUUID().toString());
		return virtualConsultation;
	}

}
