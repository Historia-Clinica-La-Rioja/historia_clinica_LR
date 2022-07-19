package net.pladema.staff.service.domain;

import java.util.List;

import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@NoArgsConstructor
public class HealthcareProfessionalCompleteBo {

	private Integer id;

	private Integer personId;

	private List<ProfessionalProfessionsBo> professionalProfessions;

	public HealthcareProfessionalCompleteBo(Integer id, Integer personId) {
		this.id = id;
		this.personId = personId;
	}
}
