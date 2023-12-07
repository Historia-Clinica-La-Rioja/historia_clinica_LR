package net.pladema.medicalconsultation.virtualConsultation.infrastructure.input.rest.dto;

import javax.validation.constraints.NotNull;

import net.pladema.medicalconsultation.virtualConsultation.domain.enums.EVirtualConsultationStatus;
import lombok.Getter;

@Getter
public class VirtualConsultationStatusDto {

	@NotNull
	private EVirtualConsultationStatus status;

}
