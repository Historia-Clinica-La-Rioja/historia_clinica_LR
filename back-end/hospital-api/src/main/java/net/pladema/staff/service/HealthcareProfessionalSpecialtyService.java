package net.pladema.staff.service;

import java.util.List;

import net.pladema.staff.service.domain.ProfessionalProfessionsBo;
import net.pladema.staff.service.domain.ProfessionalsByClinicalSpecialtyBo;

public interface HealthcareProfessionalSpecialtyService {

	List<ProfessionalsByClinicalSpecialtyBo> getProfessionalsByClinicalSpecialtyBo(List<Integer> professionalsIds);

	List<ProfessionalProfessionsBo> getProfessionsByProfessional(Integer professionalId);

	List<ProfessionalsByClinicalSpecialtyBo> getProfessionalsByActiveDiaryAndClinicalSpecialtyBo(List<Integer> professionalsIds, Integer institutionId);

}
