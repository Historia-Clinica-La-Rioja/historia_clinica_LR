package net.pladema.staff.service;

import net.pladema.staff.service.domain.ProfessionalsByClinicalSpecialtyBo;

import java.util.List;

public interface HealthcareProfessionalSpecialtyService {

    List<ProfessionalsByClinicalSpecialtyBo> getProfessionalsByClinicalSpecialtyBo(List<Integer> professionalsIds);

}
