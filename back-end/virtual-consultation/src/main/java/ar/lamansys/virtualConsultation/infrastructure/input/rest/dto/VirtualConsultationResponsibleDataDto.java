package ar.lamansys.virtualConsultation.infrastructure.input.rest.dto;

import javax.annotation.Nullable;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
public class VirtualConsultationResponsibleDataDto {

	@Nullable
	private String firstName;

	@Nullable
	private String lastName;

	private Integer healthcareProfessionalId;

	private Boolean available;

}
