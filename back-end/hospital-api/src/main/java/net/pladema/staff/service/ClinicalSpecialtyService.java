package net.pladema.staff.service;

import net.pladema.staff.service.domain.ClinicalSpecialtyBo;

import java.util.List;
import java.util.Optional;

public interface ClinicalSpecialtyService {

    Optional<ClinicalSpecialtyBo> getClinicalSpecialty(Integer clinicalSpecialtyId);

    List<ClinicalSpecialtyBo> getSpecialtiesByProfessional(Integer professionalId);
}
