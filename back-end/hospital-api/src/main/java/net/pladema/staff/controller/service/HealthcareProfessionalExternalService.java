package net.pladema.staff.controller.service;

import net.pladema.clinichistory.hospitalization.controller.dto.HealthCareProfessionalGroupDto;
import net.pladema.staff.controller.dto.ProfessionalDto;

public interface HealthcareProfessionalExternalService {

    HealthCareProfessionalGroupDto addHealthcareProfessionalGroup(Integer internmentEpisodeId, Integer healthcareProfessionalId);

    Integer getProfessionalId(Integer userId);

    ProfessionalDto findActiveProfessionalById(Integer healthCareProfessionalId);

	ProfessionalDto findFromAllProfessionalsById(Integer healthCareProfessionalId);

    ProfessionalDto findProfessionalByUserId(Integer userId);
}
