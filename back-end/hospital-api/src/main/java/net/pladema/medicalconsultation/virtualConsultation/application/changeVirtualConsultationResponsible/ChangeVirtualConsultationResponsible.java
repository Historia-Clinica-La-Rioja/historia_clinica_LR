package net.pladema.medicalconsultation.virtualConsultation.application.changeVirtualConsultationResponsible;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import net.pladema.medicalconsultation.virtualConsultation.domain.VirtualConsultationResponsibleProfessionalChangeBo;
import net.pladema.medicalconsultation.virtualConsultation.infrastructure.mqtt.VirtualConsultationPublisher;
import net.pladema.medicalconsultation.virtualConsultation.infrastructure.output.repository.VirtualConsultationRepository;
import org.springframework.stereotype.Service;

@Slf4j
@AllArgsConstructor
@Service
public class ChangeVirtualConsultationResponsible {

	private final ObjectMapper objectMapper;

	private final VirtualConsultationPublisher virtualConsultationPublisher;

	private final VirtualConsultationRepository virtualConsultationRepository;

	public Boolean run(Integer virtualConsultationId, Integer responsibleHealthcareProfessionalId) throws JsonProcessingException {
		log.debug("input parameters -> virtualConsultationId{}, responsibleHealthcareProfessionalId{}", virtualConsultationId, responsibleHealthcareProfessionalId);
		virtualConsultationRepository.updateResponsibleId(virtualConsultationId,responsibleHealthcareProfessionalId);
		VirtualConsultationResponsibleProfessionalChangeBo message = new VirtualConsultationResponsibleProfessionalChangeBo(responsibleHealthcareProfessionalId,virtualConsultationId);
		virtualConsultationPublisher.publish("CHANGE-RESPONSIBLE-PROFESSIONAL", objectMapper.writeValueAsString(message));
		return true;
	}
}
