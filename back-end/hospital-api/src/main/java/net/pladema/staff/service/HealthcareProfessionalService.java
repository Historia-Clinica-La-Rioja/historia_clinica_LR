package net.pladema.staff.service;

import net.pladema.clinichistory.hospitalization.repository.domain.HealthcareProfessionalGroup;
import net.pladema.staff.service.domain.HealthcarePersonBo;
import net.pladema.staff.service.domain.HealthcareProfessionalBo;

import java.util.List;

public interface HealthcareProfessionalService {

    List<HealthcarePersonBo> getAllDoctorsByInstitution(Integer institutionId);

    List<HealthcareProfessionalBo> getAllByInstitution(Integer institutionId);

    HealthcareProfessionalGroup addHealthcareProfessionalGroup(Integer internmentEpisodeId, Integer healthcareProfessionalId);

    Integer getProfessionalId(Integer userId);

    HealthcareProfessionalBo findProfessionalById(Integer healthcareProfessionalId);

}