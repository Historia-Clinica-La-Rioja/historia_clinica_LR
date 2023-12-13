package net.pladema.staff.application.ports;

import net.pladema.staff.domain.ProfessionalLicenseNumberBo;
import net.pladema.staff.domain.ProfessionalRegistrationNumbersBo;

import java.util.List;

public interface ProfessionalLicenseNumberStorage {

	List<ProfessionalLicenseNumberBo> getByHealthCareProfessionalId(Integer healthcareProfessionalId);

	void save(ProfessionalLicenseNumberBo bo);

	void delete(Integer professionalLicenseNumberId);

	List<ProfessionalRegistrationNumbersBo> getAllProfessionalRegistrationNumbersBo(Integer institutionId);
}
