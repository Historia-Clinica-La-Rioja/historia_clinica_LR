package ar.lamansys.virtualConsultation.application.notifyVirtualConsultationAcceptedCall;

import ar.lamansys.virtualConsultation.domain.VirtualConsultationEventBo;
import ar.lamansys.virtualConsultation.domain.enums.EVirtualConsultationEvent;
import ar.lamansys.virtualConsultation.domain.enums.EVirtualConsultationStatus;
import ar.lamansys.virtualConsultation.infrastructure.input.rest.dto.VirtualConsultationStatusDataDto;
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
public class NotifyVirtualConsultationAcceptedCallServiceImpl implements NotifyVirtualConsultationAcceptedCallService {

	private final VirtualConsultationRepository virtualConsultationRepository;

	private final VirtualConsultationPublisher virtualConsultationPublisher;

	private final ObjectMapper objectMapper;

	@Override
	public void run(Integer virtualConsultationId) {
		log.debug("Input parameters -> virtualConsultationId {}", virtualConsultationId);
		try {
			Integer professionalUserId = virtualConsultationRepository.getProfessionalUserId(virtualConsultationId);
			VirtualConsultationEventBo userMessage = new VirtualConsultationEventBo(virtualConsultationId, professionalUserId, EVirtualConsultationEvent.CALL_ACCEPTED);
			virtualConsultationPublisher.publish("NOTIFY", objectMapper.writeValueAsString(userMessage));
			//Mensaje cambiando el estado de la consulta a en progreso
			VirtualConsultationStatusDataDto generalMessage = new VirtualConsultationStatusDataDto(virtualConsultationId, EVirtualConsultationStatus.IN_PROGRESS);
			virtualConsultationPublisher.publish("CHANGE-VIRTUAL-CONSULTATION-STATE", objectMapper.writeValueAsString(generalMessage));
		}
		catch (JsonProcessingException e) {
			log.debug("Exception -> {}", e.getMessage());
		}
	}

}
