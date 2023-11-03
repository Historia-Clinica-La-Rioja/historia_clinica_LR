package net.pladema.medicalconsultation.virtualConsultation.infrastructure.input.rest.dto;

import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@NoArgsConstructor
public class VirtualConsultationResponsibleProfessionalAvailabilityDto {

	private Integer healthcareProfessionalId;

	private Integer institutionId;

	private Boolean available;

}
