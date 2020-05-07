package net.pladema.staff.controller.service;

import net.pladema.internation.controller.internment.dto.HealthCareProfessionalGroupDto;

public interface HealthcareProfessionalExternalService {

    HealthCareProfessionalGroupDto addHealthcareProfessionalGroup(Integer internmentEpisodeId, Integer healthcareProfessionalId);
}
