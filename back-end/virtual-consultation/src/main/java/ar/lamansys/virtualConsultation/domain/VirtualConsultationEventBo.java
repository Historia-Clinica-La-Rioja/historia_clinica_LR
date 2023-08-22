package ar.lamansys.virtualConsultation.domain;

import ar.lamansys.virtualConsultation.domain.enums.EVirtualConsultationEvent;

import com.fasterxml.jackson.annotation.JsonProperty;

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
