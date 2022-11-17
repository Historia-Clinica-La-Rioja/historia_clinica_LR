package net.pladema.staff.service;

import net.pladema.staff.service.domain.ClinicalSpecialtyBo;

import java.util.List;
import java.util.Optional;

public interface ClinicalSpecialtyService {

    Optional<ClinicalSpecialtyBo> getClinicalSpecialty(Integer clinicalSpecialtyId);

    List<ClinicalSpecialtyBo> getSpecialtiesByProfessional(Integer professionalId);

    List<ClinicalSpecialtyBo> getAll();

	List<ClinicalSpecialtyBo> getAllByInstitutionIdAndActiveDiaries(Integer institutionId);

	List<ClinicalSpecialtyBo> getClinicalSpecialtiesByCareLineIdAndDestinationIntitutionId(Integer careLineId, Integer destinationInstitutionId);

}
