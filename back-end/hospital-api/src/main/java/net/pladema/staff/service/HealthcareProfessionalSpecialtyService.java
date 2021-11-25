package net.pladema.staff.service;

import net.pladema.staff.service.domain.HealthcareProfessionalSpecialtyBo;
import net.pladema.staff.service.domain.ProfessionalsByClinicalSpecialtyBo;

import java.util.List;

public interface HealthcareProfessionalSpecialtyService {

    List<ProfessionalsByClinicalSpecialtyBo> getProfessionalsByClinicalSpecialtyBo(List<Integer> professionalsIds);

    List<HealthcareProfessionalSpecialtyBo> getProfessionsByProfessional(Integer professionalId);

    Integer saveProfessionalSpeciality(HealthcareProfessionalSpecialtyBo healthcareProfessionalSpecialtyBo);

    void delete(Integer id);
}
