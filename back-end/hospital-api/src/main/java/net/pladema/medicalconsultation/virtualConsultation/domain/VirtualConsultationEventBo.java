package net.pladema.medicalconsultation.virtualConsultation.domain;

import net.pladema.medicalconsultation.virtualConsultation.domain.enums.EVirtualConsultationEvent;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
public class VirtualConsultationEventBo {

	private Integer virtualConsultationId;

	private Integer destinationUserId;

	private EVirtualConsultationEvent event;

}
