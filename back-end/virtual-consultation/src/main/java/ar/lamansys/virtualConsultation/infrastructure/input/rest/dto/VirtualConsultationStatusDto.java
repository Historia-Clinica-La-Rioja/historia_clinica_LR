package ar.lamansys.virtualConsultation.infrastructure.input.rest.dto;

import javax.validation.constraints.NotNull;

import ar.lamansys.virtualConsultation.domain.enums.EVirtualConsultationStatus;
import lombok.Getter;

@Getter
public class VirtualConsultationStatusDto {

	@NotNull
	private EVirtualConsultationStatus status;

}
