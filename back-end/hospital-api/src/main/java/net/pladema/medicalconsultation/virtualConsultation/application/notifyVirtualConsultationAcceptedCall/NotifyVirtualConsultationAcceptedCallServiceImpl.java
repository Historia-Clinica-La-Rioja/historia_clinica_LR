package net.pladema.medicalconsultation.virtualConsultation.application.notifyVirtualConsultationAcceptedCall;

import net.pladema.medicalconsultation.virtualConsultation.application.changeVirtualConsultationStatus.ChangeVirtualConsultationStatusService;
import net.pladema.medicalconsultation.virtualConsultation.domain.VirtualConsultationEventBo;
import net.pladema.medicalconsultation.virtualConsultation.domain.enums.EVirtualConsultationEvent;
import net.pladema.medicalconsultation.virtualConsultation.domain.enums.EVirtualConsultationStatus;
import net.pladema.medicalconsultation.virtualConsultation.infrastructure.input.rest.dto.VirtualConsultationStatusDataDto;
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
public class NotifyVirtualConsultationAcceptedCallServiceImpl implements NotifyVirtualConsultationAcceptedCallService {

	private final VirtualConsultationRepository virtualConsultationRepository;

	private final VirtualConsultationPublisher virtualConsultationPublisher;

	private final ObjectMapper objectMapper;

	private final ChangeVirtualConsultationStatusService changeVirtualConsultationStatusService;

	private final EVirtualConsultationStatus IN_PROGRESS_STATUS = EVirtualConsultationStatus.IN_PROGRESS;

	@Override
	public void run(Integer virtualConsultationId) {
		log.debug("Input parameters -> virtualConsultationId {}", virtualConsultationId);
		try {
			Integer professionalUserId = virtualConsultationRepository.getProfessionalUserId(virtualConsultationId);
			VirtualConsultationEventBo userMessage = new VirtualConsultationEventBo(virtualConsultationId, professionalUserId, EVirtualConsultationEvent.CALL_ACCEPTED);
			virtualConsultationPublisher.publish("NOTIFY", objectMapper.writeValueAsString(userMessage));
			//Mensaje cambiando el estado de la consulta a en progreso
			changeVirtualConsultationStatusService.run(virtualConsultationId, IN_PROGRESS_STATUS);
			VirtualConsultationStatusDataDto generalMessage = new VirtualConsultationStatusDataDto(virtualConsultationId, IN_PROGRESS_STATUS);
			virtualConsultationPublisher.publish("CHANGE-VIRTUAL-CONSULTATION-STATE", objectMapper.writeValueAsString(generalMessage));
		}
		catch (JsonProcessingException e) {
			log.debug("Exception -> {}", e.getMessage());
		}
	}

}
