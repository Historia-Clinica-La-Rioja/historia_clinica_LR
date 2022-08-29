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

	private Integer clinicalSpecialtyId;

	private boolean deleted;

	public ProfessionalProfessionBackofficeDto(Integer id, Integer healthcareProfessionalId, Integer personId, Integer professionalSpecialtyId, boolean deleted) {
		this.id = id;
		this.healthcareProfessionalId = healthcareProfessionalId;
		this.personId = personId;
		this.professionalSpecialtyId = professionalSpecialtyId;
		this.deleted = deleted;
	}
}
