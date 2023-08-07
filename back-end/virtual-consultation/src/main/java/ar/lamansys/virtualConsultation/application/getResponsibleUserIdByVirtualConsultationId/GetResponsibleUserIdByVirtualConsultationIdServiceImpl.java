package ar.lamansys.virtualConsultation.application.getResponsibleUserIdByVirtualConsultationId;

import org.springframework.stereotype.Service;

import ar.lamansys.virtualConsultation.infrastructure.output.repository.VirtualConsultationRepository;
import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;

@Slf4j
@AllArgsConstructor
@Service
public class GetResponsibleUserIdByVirtualConsultationIdServiceImpl implements GetResponsibleUserIdByVirtualConsultationIdService {

	private final VirtualConsultationRepository virtualConsultationRepository;

	@Override
	public Integer run(Integer virtualConsultationId) {
		log.debug("Input parameters -> virtualConsultationId {}", virtualConsultationId);
		Integer result = virtualConsultationRepository.getResponsibleUserId(virtualConsultationId);
		log.debug("Output -> {}", result);
		return result;
	}

}
