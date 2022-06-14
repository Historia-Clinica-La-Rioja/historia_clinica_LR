package net.pladema.staff.controller.dto;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@AllArgsConstructor
@NoArgsConstructor
@Setter
public class ProfessionalProfessionBackofficeDto {

	private Integer id;

	private Integer healthcareProfessionalId;

	private Integer personId;

	private Integer professionalSpecialtyId;

	private boolean deleted;
}
