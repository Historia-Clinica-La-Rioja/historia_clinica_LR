package ar.lamansys.virtualConsultation.infrastructure.input.rest.dto;

import ar.lamansys.virtualConsultation.domain.VirtualConsultationEventBo;
import ar.lamansys.virtualConsultation.domain.enums.EVirtualConsultationEvent;
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
