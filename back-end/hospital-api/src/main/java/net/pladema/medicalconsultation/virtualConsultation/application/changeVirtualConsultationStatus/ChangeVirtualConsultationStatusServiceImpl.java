package net.pladema.medicalconsultation.virtualConsultation.application.changeVirtualConsultationStatus;

import org.springframework.stereotype.Service;

import net.pladema.medicalconsultation.virtualConsultation.domain.enums.EVirtualConsultationStatus;
import net.pladema.medicalconsultation.virtualConsultation.infrastructure.output.repository.VirtualConsultationRepository;
import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;

@Slf4j
@AllArgsConstructor
@Service
public class ChangeVirtualConsultationStatusServiceImpl implements ChangeVirtualConsultationStatusService {

	private final VirtualConsultationRepository virtualConsultationRepository;

	@Override
	public Boolean run(Integer virtualConsultationId, EVirtualConsultationStatus status) {
		log.debug("Input parameters -> virtualConsultationId {}, status {}", virtualConsultationId, status);
		virtualConsultationRepository.updateVirtualConsultationStatus(virtualConsultationId, status.getId());
		return true;
	}

}
