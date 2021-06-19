package net.pladema.staff.service;

import net.pladema.staff.service.domain.ClinicalSpecialtyBo;

import java.util.Optional;

public interface ClinicalSpecialtyService {

    Optional<ClinicalSpecialtyBo> getClinicalSpecialty(Integer clinicalSpecialtyId);
}
