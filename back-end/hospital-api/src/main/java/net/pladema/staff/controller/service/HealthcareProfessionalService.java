package net.pladema.staff.controller.service;

import net.pladema.internation.controller.internment.dto.HealthCareProfessionalGroupDto;

public interface HealthcareProfessionalService {

    HealthCareProfessionalGroupDto addHealthcareProfessionalGroup(Integer internmentEpisodeId, Integer healthcareProfessionalId);
}