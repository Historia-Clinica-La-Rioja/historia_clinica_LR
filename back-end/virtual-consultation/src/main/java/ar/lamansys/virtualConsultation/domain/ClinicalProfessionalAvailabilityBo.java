package ar.lamansys.virtualConsultation.domain;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
@AllArgsConstructor
public class ClinicalProfessionalAvailabilityBo {

	private Integer healthcareProfessionalId;

	private Boolean available;

}
