package net.pladema.medicalconsultation.virtualConsultation.infrastructure.input.rest.dto;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class VirtualConsultationAvailableProfessionalAmountDto {

	private Integer virtualConsultationId;

	private Integer professionalAmount;

}
