package ar.lamansys.virtualConsultation.infrastructure.input.rest.dto;

import ar.lamansys.virtualConsultation.domain.enums.EVirtualConsultationResponsibleProfessionalAvailability;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@NoArgsConstructor
public class VirtualConsultationResponsibleProfessionalAvailabilityDto {

	private Integer healthcareProfessionalId;

	private Integer institutionId;

	private EVirtualConsultationResponsibleProfessionalAvailability available;

}
