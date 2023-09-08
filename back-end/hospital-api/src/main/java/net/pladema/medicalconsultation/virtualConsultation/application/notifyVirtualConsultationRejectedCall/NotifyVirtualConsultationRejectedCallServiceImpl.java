package net.pladema.medicalconsultation.virtualConsultation.application.notifyVirtualConsultationRejectedCall;

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
public class NotifyVirtualConsultationRejectedCallServiceImpl implements NotifyVirtualConsultationRejectedCallService {

	private final VirtualConsultationRepository virtualConsultationRepository;

	private final VirtualConsultationPublisher virtualConsultationPublisher;

	private final ObjectMapper objectMapper;

	@Override
	public void run(Integer virtualConsultationId) {
		log.debug("Input parameters -> virtualConsultationId {}", virtualConsultationId);
		try {
			Integer professionalUserId = virtualConsultationRepository.getProfessionalUserId(virtualConsultationId);
			virtualConsultationRepository.removeProfessionalByVirtualConsultationId(virtualConsultationId);
			VirtualConsultationEventBo message = new VirtualConsultationEventBo(virtualConsultationId, professionalUserId, EVirtualConsultationEvent.CALL_REJECTED);
			virtualConsultationPublisher.publish("NOTIFY", objectMapper.writeValueAsString(message));
		}
		catch (JsonProcessingException e) {
			log.debug("Exception -> {}", e.getMessage());
		}
	}

}
