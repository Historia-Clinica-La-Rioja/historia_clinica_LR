package net.pladema.staff.service;

import net.pladema.clinichistory.hospitalization.repository.domain.HealthcareProfessionalGroup;
import net.pladema.staff.service.domain.HealthcareProfessionalBo;

import java.util.List;

public interface HealthcareProfessionalService {

    List<HealthcareProfessionalBo> getAll(Integer institutionId);

    HealthcareProfessionalGroup addHealthcareProfessionalGroup(Integer internmentEpisodeId, Integer healthcareProfessionalId);

    Integer getProfessionalId(Integer userId);
}