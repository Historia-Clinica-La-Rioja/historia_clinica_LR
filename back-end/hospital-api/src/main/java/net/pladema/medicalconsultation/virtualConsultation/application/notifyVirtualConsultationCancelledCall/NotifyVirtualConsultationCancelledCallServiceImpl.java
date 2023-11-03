package net.pladema.medicalconsultation.virtualConsultation.application.notifyVirtualConsultationCancelledCall;

import net.pladema.medicalconsultation.virtualConsultation.domain.enums.EVirtualConsultationEvent;
import net.pladema.medicalconsultation.virtualConsultation.domain.VirtualConsultationEventBo;
import net.pladema.medicalconsultation.virtualConsultation.infrastructure.mqtt.VirtualConsultationPublisher;
import net.pladema.medicalconsultation.virtualConsultation.infrastructure.output.repository.VirtualConsultationRepository;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;

import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;

import org.springframework.stereotype.Service;

@Slf4j
@AllArgsConstructor
@Service
public class NotifyVirtualConsultationCancelledCallServiceImpl implements NotifyVirtualConsultationCancelledCallService {

	private final VirtualConsultationRepository virtualConsultationRepository;

	private final VirtualConsultationPublisher virtualConsultationPublisher;

	private final ObjectMapper objectMapper;

	@Override
	public void run(Integer virtualConsultationId) {
		log.debug("Input parameters -> virtualConsultationId {}", virtualConsultationId);
		try {
			virtualConsultationRepository.removeProfessionalByVirtualConsultationId(virtualConsultationId);
			Integer responsibleUserId = virtualConsultationRepository.getResponsibleUserId(virtualConsultationId);
			VirtualConsultationEventBo message = new VirtualConsultationEventBo(virtualConsultationId, responsibleUserId, EVirtualConsultationEvent.CALL_CANCELED);
			virtualConsultationPublisher.publish("NOTIFY", objectMapper.writeValueAsString(message));
		}
		catch (JsonProcessingException e) {
			log.debug("Exception -> {}", e.getMessage());
		}
	}

}
