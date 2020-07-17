package net.pladema.staff.service;

import net.pladema.clinichistory.hospitalization.repository.domain.HealthcareProfessionalGroup;

public interface HealthcareProfessionalService {

    HealthcareProfessionalGroup addHealthcareProfessionalGroup(Integer internmentEpisodeId, Integer healthcareProfessionalId);

    Integer getProfessionalId(Integer userId);
}