package ar.lamansys.virtualConsultation.infrastructure.input.rest.dto;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
public class VirtualConsultationResponsibleDataDto {

	private String firstName;

	private String lastName;

	private Integer healthcareProfessionalId;

	private Boolean available;

}
