package net.pladema.staff.repository.domain;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Setter
@Getter
@NoArgsConstructor
@AllArgsConstructor
public class HealthcareProfessionalSpecialtyVo {

    private Integer id;

    private Integer healthcareProfessionalId;

    private Integer professionalProfessionId;

    private Integer clinicalSpecialtyId;

	private String clinicalSpecialtyName;
}
