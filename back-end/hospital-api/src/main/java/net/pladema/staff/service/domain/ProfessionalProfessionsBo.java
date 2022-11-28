package net.pladema.staff.service.domain;

import java.util.List;

import javax.annotation.Nullable;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import net.pladema.staff.repository.domain.ProfessionalProfessionsVo;

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
	
	public ProfessionalProfessionsBo (ProfessionalProfessionsVo vo){
		this.id = vo.getId();
		this.healthcareProfessionalId = vo.getHealthcareProfessionalId();
		this.profession = new ProfessionalSpecialtyBo(vo.getProfessionalSpecialtyId(), vo.getProfessionalSpecialtyName());
	}

}
