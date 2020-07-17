package net.pladema.staff.controller.service;

import net.pladema.clinichistory.hospitalization.controller.dto.HealthCareProfessionalGroupDto;

public interface HealthcareProfessionalExternalService {

    HealthCareProfessionalGroupDto addHealthcareProfessionalGroup(Integer internmentEpisodeId, Integer healthcareProfessionalId);

    Integer getProfessionalId(Integer userId);
}
