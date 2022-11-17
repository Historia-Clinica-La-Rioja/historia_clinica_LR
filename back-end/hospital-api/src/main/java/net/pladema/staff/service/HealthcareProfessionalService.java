package net.pladema.staff.service;

import net.pladema.clinichistory.hospitalization.repository.domain.HealthcareProfessionalGroup;
import net.pladema.staff.service.domain.HealthcarePersonBo;
import net.pladema.staff.service.domain.HealthcareProfessionalBo;
import net.pladema.staff.service.domain.HealthcareProfessionalCompleteBo;

import java.util.List;
import java.util.Optional;

public interface HealthcareProfessionalService {

    List<HealthcareProfessionalBo> getAllProfessional();

    List<HealthcarePersonBo> getAllDoctorsByInstitution(Integer institutionId);

    List<HealthcareProfessionalBo> getAllByInstitution(Integer institutionId);

    HealthcareProfessionalGroup addHealthcareProfessionalGroup(Integer internmentEpisodeId, Integer healthcareProfessionalId);

    Integer getProfessionalId(Integer userId);

    HealthcareProfessionalBo findActiveProfessionalById(Integer healthcareProfessionalId);

	HealthcareProfessionalBo findFromAllProfessionalsById(Integer healthcareProfessionalId);

    HealthcareProfessionalBo findProfessionalByPersonId(Integer personId);

    Integer saveProfessional(HealthcareProfessionalCompleteBo professionalCompleteBo);

	Optional<Integer> getProfessionalIdByPersonId(Integer personId);
}