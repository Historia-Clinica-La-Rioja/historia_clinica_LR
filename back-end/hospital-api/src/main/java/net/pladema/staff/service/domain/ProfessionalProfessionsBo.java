package net.pladema.staff.service.domain;

import java.util.List;

import javax.annotation.Nullable;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
public class ProfessionalProfessionsBo {

	@Nullable
	private Integer id;

	private Integer healthcareProfessionalId;

	private ProfessionalSpecialtyBo profession;

	private List<HealthcareProfessionalSpecialtyBo> specialties;

	public boolean equalsProfession(ProfessionalProfessionsBo bo){
		return this.profession.getId().equals(bo.getProfession().getId());
	}

}
