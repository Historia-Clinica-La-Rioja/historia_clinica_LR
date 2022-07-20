package net.pladema.staff.application.ports;

import net.pladema.staff.domain.ProfessionalLicenseNumberBo;

import java.util.List;

public interface ProfessionalLicenseNumberStorage {

	List<ProfessionalLicenseNumberBo> getByHealthCareProfessionalId(Integer healthcareProfessionalId);

	void save(ProfessionalLicenseNumberBo bo);

	void delete(Integer professionalLicenseNumberId);
}
