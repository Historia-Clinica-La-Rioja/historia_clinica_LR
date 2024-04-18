package net.pladema.staff.controller.service;

import ar.lamansys.sgh.shared.infrastructure.input.service.staff.ProfessionalCompleteDto;
import net.pladema.clinichistory.hospitalization.controller.dto.HealthCareProfessionalGroupDto;
import net.pladema.staff.controller.dto.ProfessionalDto;

import java.util.List;

public interface HealthcareProfessionalExternalService {

    HealthCareProfessionalGroupDto addHealthcareProfessionalGroup(Integer internmentEpisodeId, Integer healthcareProfessionalId);

    Integer getProfessionalId(Integer userId);

    ProfessionalDto findActiveProfessionalById(Integer healthCareProfessionalId);

	ProfessionalDto findFromAllProfessionalsById(Integer healthCareProfessionalId);

    ProfessionalDto findProfessionalByUserId(Integer userId);

	ProfessionalCompleteDto getProfessionalCompleteInfoByUser(Integer userId);

	ProfessionalCompleteDto getProfessionalCompleteInfoById(Integer professionalId);

	List<ProfessionalCompleteDto> getProfessionalsCompleteInfoByIds(List<Integer> professionalIds);
}
