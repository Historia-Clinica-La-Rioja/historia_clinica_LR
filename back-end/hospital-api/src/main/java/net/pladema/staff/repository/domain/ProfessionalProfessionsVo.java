package net.pladema.staff.repository.domain;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
@AllArgsConstructor
public class ProfessionalProfessionsVo {

	private Integer id;

	private Integer healthcareProfessionalId;

	private Integer professionalSpecialtyId;

	private String professionalSpecialtyName;

}
