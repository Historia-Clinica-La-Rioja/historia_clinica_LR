package net.pladema.medicalconsultation.virtualConsultation.infrastructure.input.rest.dto;

import net.pladema.medicalconsultation.virtualConsultation.domain.VirtualConsultationEventBo;
import net.pladema.medicalconsultation.virtualConsultation.domain.enums.EVirtualConsultationEvent;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class VirtualConsultationEventDto {

	private Integer virtualConsultationId;

	private EVirtualConsultationEvent event;

	public VirtualConsultationEventDto(VirtualConsultationEventBo virtualConsultationEvent) {
		this.virtualConsultationId = virtualConsultationEvent.getVirtualConsultationId();
		this.event = virtualConsultationEvent.getEvent();
	}

}
