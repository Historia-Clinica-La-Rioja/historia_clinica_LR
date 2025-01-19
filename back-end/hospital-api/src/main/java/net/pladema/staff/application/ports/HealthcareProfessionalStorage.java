package net.pladema.staff.application.ports;

import net.pladema.staff.domain.HealthcareProfessionalSearchBo;
import net.pladema.staff.domain.ProfessionalCompleteBo;
import net.pladema.staff.service.domain.HealthcareProfessionalBo;

import java.util.List;

public interface HealthcareProfessionalStorage {
	ProfessionalCompleteBo fetchProfessionalByUserId(Integer userId);
	ProfessionalCompleteBo fetchProfessionalById(Integer professionalId);
    List<ProfessionalCompleteBo> fetchProfessionalsByIds(List<Integer> professionalIds);
	List<HealthcareProfessionalBo> fetchAllProfessionalsByFilter(HealthcareProfessionalSearchBo searchCriteria, List<Short> professionalERolIds);
}
