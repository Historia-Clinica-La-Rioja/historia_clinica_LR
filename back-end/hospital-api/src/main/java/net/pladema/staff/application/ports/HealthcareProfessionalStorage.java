package net.pladema.staff.application.ports;

import net.pladema.staff.domain.ProfessionalCompleteBo;

public interface HealthcareProfessionalStorage {
	ProfessionalCompleteBo fetchProfessionalByUserId(Integer userId);
	ProfessionalCompleteBo fetchProfessionalById(Integer professionalId);
}
