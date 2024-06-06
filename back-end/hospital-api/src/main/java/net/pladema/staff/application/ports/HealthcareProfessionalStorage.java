package net.pladema.staff.application.ports;

import net.pladema.staff.domain.ProfessionalCompleteBo;

import java.util.List;

public interface HealthcareProfessionalStorage {
	ProfessionalCompleteBo fetchProfessionalByUserId(Integer userId);
	ProfessionalCompleteBo fetchProfessionalById(Integer professionalId);
    List<ProfessionalCompleteBo> fetchProfessionalsByIds(List<Integer> professionalIds);
}
