package net.pladema.staff.application.ports;

import java.util.List;

import net.pladema.staff.service.domain.ProfessionalProfessionsBo;

public interface ProfessionalProfessionStorage {

	List<ProfessionalProfessionsBo> getProfessionsByHealthcareProfessionalId(Integer healthcareProfessionalId);

	void delete(Integer id);

	void deleteHealthcareProfessionalSpecialty(Integer id);

	Integer save(ProfessionalProfessionsBo bo);
}
