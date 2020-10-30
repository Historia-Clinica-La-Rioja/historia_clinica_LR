package net.pladema.staff.service;

import net.pladema.clinichistory.hospitalization.service.domain.ClinicalSpecialtyBo;

import java.util.Optional;

public interface ClinicalSpecialtyService {

    Optional<ClinicalSpecialtyBo> getClinicalSpecialty(Integer clinicalSpecialtyId);
}
