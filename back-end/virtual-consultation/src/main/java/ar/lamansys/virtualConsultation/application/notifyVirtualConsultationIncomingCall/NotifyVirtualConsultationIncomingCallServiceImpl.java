package ar.lamansys.virtualConsultation.application.notifyVirtualConsultationIncomingCall;

import ar.lamansys.virtualConsultation.domain.enums.EVirtualConsultationEvent;
import ar.lamansys.virtualConsultation.domain.VirtualConsultationEventBo;
import ar.lamansys.virtualConsultation.infrastructure.mqtt.VirtualConsultationPublisher;
import ar.lamansys.virtualConsultation.infrastructure.output.repository.VirtualConsultationRepository;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;

import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;

import org.springframework.stereotype.Service;

@Slf4j
@AllArgsConstructor
@Service
public class NotifyVirtualConsultationIncomingCallServiceImpl implements NotifyVirtualConsultationIncomingCallService {

	private final VirtualConsultationRepository virtualConsultationRepository;

	private final VirtualConsultationPublisher virtualConsultationPublisher;

	private final ObjectMapper objectMapper;

	@Override
	public void run(Integer virtualConsultationId, Integer healthcareProfessionalId) {
		log.debug("Input parameters -> virtualConsultationId {}, healthcareProfessionalId {}", virtualConsultationId, healthcareProfessionalId);
		try {
			virtualConsultationRepository.updateProfessionalId(virtualConsultationId, healthcareProfessionalId);
			Integer responsibleUserId = virtualConsultationRepository.getResponsibleUserId(virtualConsultationId);
			VirtualConsultationEventBo message = new VirtualConsultationEventBo(virtualConsultationId, responsibleUserId, EVirtualConsultationEvent.INCOMING_CALL);
			virtualConsultationPublisher.publish("NOTIFY", objectMapper.writeValueAsString(message));
		}
		catch (JsonProcessingException e) {
			log.debug("Exception -> {}", e.getMessage());
		}
	}

}
