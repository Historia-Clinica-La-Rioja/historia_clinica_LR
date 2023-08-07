package ar.lamansys.virtualConsultation.application.saveVirtualConsultation;

import java.util.UUID;

import org.springframework.stereotype.Service;

import ar.lamansys.virtualConsultation.domain.VirtualConsultationRequestBo;
import ar.lamansys.virtualConsultation.infrastructure.output.repository.VirtualConsultationRepository;
import ar.lamansys.virtualConsultation.infrastructure.output.repository.entity.VirtualConsultation;
import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;

@Service
@Slf4j
@AllArgsConstructor
public class SaveVirtualConsultationRequestServiceImpl implements SaveVirtualConsultationRequestService {

	private final VirtualConsultationRepository virtualConsultationRepository;

	@Override
	public Integer run(VirtualConsultationRequestBo virtualConsultation) {
		log.debug("Input parameters -> virtualCOnsultation {}", virtualConsultation);
		Integer result = virtualConsultationRepository.save(mapToEntity(virtualConsultation)).getId();
		log.debug("Output -> {}", result);
		return result;
	}

	private VirtualConsultation mapToEntity(VirtualConsultationRequestBo virtualConsultationRequestBo) {
		VirtualConsultation virtualConsultation = new VirtualConsultation(virtualConsultationRequestBo);
		virtualConsultation.setCallId(UUID.randomUUID().toString());
		return virtualConsultation;
	}

}
