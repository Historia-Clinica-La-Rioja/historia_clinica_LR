package ar.lamansys.virtualConsultation.domain;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
@AllArgsConstructor
public class VirtualConsultationResponsibleProfessionalAvailabilityBo {

	private Integer healthcareProfessionalId;

	private Integer institutionId;

	private Boolean available;

}
